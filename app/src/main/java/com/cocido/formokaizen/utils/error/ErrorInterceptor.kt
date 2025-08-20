package com.cocido.formokaizen.utils.error

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.delay
import com.cocido.formokaizen.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorInterceptor @Inject constructor(
    private val errorHandler: ErrorHandler
) {

    fun <T> interceptFlow(
        flow: Flow<T>,
        maxRetries: Int = 3,
        enableRetry: Boolean = true
    ): Flow<Resource<T>> {
        return flow
            .map<T, Resource<T>> { Resource.Success(it) }
            .onStart { emit(Resource.Loading()) }
            .retryWhen { cause, attempt ->
                val appError = errorHandler.handleError(cause)
                
                if (enableRetry && attempt < maxRetries && errorHandler.shouldRetry(appError)) {
                    val delayTime = errorHandler.getRetryDelay(attempt.toInt() + 1)
                    appError.logError("ErrorInterceptor")
                    delay(delayTime)
                    true
                } else {
                    false
                }
            }
            .catch { cause ->
                val appError = errorHandler.handleError(cause)
                appError.logError("ErrorInterceptor")
                emit(Resource.Error(appError.userMessage))
            }
    }

    suspend fun <T> interceptSuspend(
        block: suspend () -> T,
        maxRetries: Int = 3,
        enableRetry: Boolean = true
    ): Resource<T> {
        var lastError: AppError? = null
        
        repeat(if (enableRetry) maxRetries else 1) { attempt ->
            try {
                val result = block()
                return Resource.Success(result)
            } catch (throwable: Throwable) {
                val appError = errorHandler.handleError(throwable)
                lastError = appError
                
                if (enableRetry && attempt < maxRetries - 1 && errorHandler.shouldRetry(appError)) {
                    val delayTime = errorHandler.getRetryDelay(attempt + 1)
                    appError.logError("ErrorInterceptor")
                    delay(delayTime)
                } else {
                    appError.logError("ErrorInterceptor")
                    return Resource.Error(appError.userMessage)
                }
            }
        }
        
        return Resource.Error(lastError?.userMessage ?: "Error desconocido")
    }
}

// Extensiones para facilitar el uso
fun <T> Flow<T>.handleErrors(
    errorInterceptor: ErrorInterceptor,
    maxRetries: Int = 3,
    enableRetry: Boolean = true
): Flow<Resource<T>> {
    return errorInterceptor.interceptFlow(this, maxRetries, enableRetry)
}

suspend fun <T> ErrorInterceptor.safeCall(
    maxRetries: Int = 3,
    enableRetry: Boolean = true,
    block: suspend () -> T
): Resource<T> {
    return interceptSuspend(block, maxRetries, enableRetry)
}