package com.cocido.formokaizen.utils.error

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.cocido.formokaizen.R
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

sealed class AppError(
    override val message: String,
    val userMessage: String,
    val errorCode: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    class NetworkError(
        message: String = "Error de red",
        userMessage: String = "Problema de conexión. Verifica tu internet e intenta nuevamente.",
        cause: Throwable? = null
    ) : AppError(message, userMessage, "NETWORK_ERROR", cause)

    class ServerError(
        message: String = "Error del servidor",
        userMessage: String = "El servidor no está disponible. Intenta más tarde.",
        errorCode: String? = null,
        cause: Throwable? = null
    ) : AppError(message, userMessage, errorCode, cause)

    class AuthenticationError(
        message: String = "Error de autenticación",
        userMessage: String = "Tu sesión ha expirado. Inicia sesión nuevamente.",
        cause: Throwable? = null
    ) : AppError(message, userMessage, "AUTH_ERROR", cause)

    class ValidationError(
        message: String,
        userMessage: String = message,
        cause: Throwable? = null
    ) : AppError(message, userMessage, "VALIDATION_ERROR", cause)

    class NotFoundError(
        message: String = "Recurso no encontrado",
        userMessage: String = "El elemento solicitado no existe o fue eliminado.",
        cause: Throwable? = null
    ) : AppError(message, userMessage, "NOT_FOUND_ERROR", cause)

    class PermissionError(
        message: String = "Sin permisos",
        userMessage: String = "No tienes permisos para realizar esta acción.",
        cause: Throwable? = null
    ) : AppError(message, userMessage, "PERMISSION_ERROR", cause)

    class DatabaseError(
        message: String = "Error de base de datos",
        userMessage: String = "Error al acceder a los datos locales.",
        cause: Throwable? = null
    ) : AppError(message, userMessage, "DATABASE_ERROR", cause)

    class FileError(
        message: String = "Error de archivo",
        userMessage: String = "Error al procesar el archivo.",
        cause: Throwable? = null
    ) : AppError(message, userMessage, "FILE_ERROR", cause)

    class UnknownError(
        message: String = "Error desconocido",
        userMessage: String = "Ocurrió un error inesperado. Intenta nuevamente.",
        cause: Throwable? = null
    ) : AppError(message, userMessage, "UNKNOWN_ERROR", cause)
}

@Singleton
class ErrorHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun handleError(throwable: Throwable): AppError {
        return when (throwable) {
            is AppError -> throwable
            
            is HttpException -> handleHttpException(throwable)
            
            is SocketTimeoutException -> AppError.NetworkError(
                message = "Timeout de conexión",
                userMessage = "Tiempo de conexión agotado. Intenta nuevamente.",
                cause = throwable
            )
            
            is UnknownHostException -> AppError.NetworkError(
                message = "Host desconocido",
                userMessage = "Sin conexión a internet. Verifica tu conexión.",
                cause = throwable
            )
            
            is IOException -> AppError.NetworkError(
                message = "Error de entrada/salida",
                userMessage = "Error de red. Verifica tu conexión e intenta nuevamente.",
                cause = throwable
            )
            
            else -> AppError.UnknownError(
                message = throwable.message ?: "Error desconocido",
                userMessage = "Ocurrió un error inesperado. Intenta nuevamente.",
                cause = throwable
            )
        }
    }

    private fun handleHttpException(exception: HttpException): AppError {
        return when (exception.code()) {
            400 -> AppError.ValidationError(
                message = "Solicitud inválida",
                userMessage = "Solicitud inválida. Verifica los datos ingresados.",
                cause = exception
            )
            
            401 -> AppError.AuthenticationError(
                message = "No autorizado",
                userMessage = "No autorizado. Inicia sesión nuevamente.",
                cause = exception
            )
            
            403 -> AppError.PermissionError(
                message = "Prohibido",
                userMessage = "Acceso denegado. No tienes permisos para esta acción.",
                cause = exception
            )
            
            404 -> AppError.NotFoundError(
                message = "No encontrado",
                userMessage = "Recurso no encontrado.",
                cause = exception
            )
            
            422 -> {
                val errorBody = parseErrorBody(exception)
                AppError.ValidationError(
                    message = errorBody?.message ?: "Error de validación",
                    userMessage = errorBody?.userMessage ?: "Error de validación. Verifica los datos ingresados.",
                    cause = exception
                )
            }
            
            in 500..599 -> AppError.ServerError(
                message = "Error del servidor: ${exception.code()}",
                userMessage = "Error del servidor. Intenta más tarde.",
                errorCode = exception.code().toString(),
                cause = exception
            )
            
            else -> AppError.UnknownError(
                message = "Error HTTP: ${exception.code()}",
                userMessage = "Error de conexión. Código: ${exception.code()}",
                cause = exception
            )
        }
    }

    private fun parseErrorBody(exception: HttpException): ErrorResponse? {
        return try {
            // Aquí normalmente parsearías el JSON del error
            // Por ahora retornamos null para usar mensajes por defecto
            null
        } catch (e: Exception) {
            null
        }
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
               capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
               capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    fun shouldRetry(error: AppError): Boolean {
        return when (error) {
            is AppError.NetworkError -> true
            is AppError.ServerError -> error.errorCode in listOf("500", "502", "503", "504")
            else -> false
        }
    }

    fun getRetryDelay(attemptCount: Int): Long {
        // Exponential backoff: 1s, 2s, 4s, 8s, máximo 30s
        return minOf(1000L * (1 shl (attemptCount - 1)), 30000L)
    }
}

data class ErrorResponse(
    val message: String,
    val userMessage: String,
    val errorCode: String? = null,
    val details: Map<String, Any>? = null
)

// Extensión para logging de errores
fun AppError.logError(tag: String = "FormoKaizen") {
    android.util.Log.e(tag, "Error: $message, Code: $errorCode", cause)
}