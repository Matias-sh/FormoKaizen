package com.cocido.formokaizen.utils.validators

import android.util.Patterns
import java.time.LocalDateTime

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}

class FormValidator {

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Invalid("El email es requerido")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult.Invalid("Formato de email inválido")
            email.length > 254 -> ValidationResult.Invalid("El email es demasiado largo")
            else -> ValidationResult.Valid
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid("La contraseña es requerida")
            password.length < 8 -> ValidationResult.Invalid("La contraseña debe tener al menos 8 caracteres")
            password.length > 128 -> ValidationResult.Invalid("La contraseña es demasiado larga")
            !password.any { it.isUpperCase() } -> ValidationResult.Invalid("Debe contener al menos una mayúscula")
            !password.any { it.isLowerCase() } -> ValidationResult.Invalid("Debe contener al menos una minúscula")
            !password.any { it.isDigit() } -> ValidationResult.Invalid("Debe contener al menos un número")
            !password.any { it in "!@#$%^&*()_+-=[]{}|;:,.<>?" } -> ValidationResult.Invalid("Debe contener al menos un carácter especial")
            else -> ValidationResult.Valid
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult.Invalid("Confirme la contraseña")
            password != confirmPassword -> ValidationResult.Invalid("Las contraseñas no coinciden")
            else -> ValidationResult.Valid
        }
    }

    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult.Invalid("El nombre de usuario es requerido")
            username.length < 3 -> ValidationResult.Invalid("Debe tener al menos 3 caracteres")
            username.length > 30 -> ValidationResult.Invalid("Debe tener máximo 30 caracteres")
            !username.matches(Regex("^[a-zA-Z0-9._-]+$")) -> ValidationResult.Invalid("Solo letras, números, puntos, guiones y guiones bajos")
            username.startsWith(".") || username.endsWith(".") -> ValidationResult.Invalid("No puede empezar o terminar con punto")
            username.contains("..") -> ValidationResult.Invalid("No puede contener puntos consecutivos")
            else -> ValidationResult.Valid
        }
    }

    fun validateName(name: String, fieldName: String = "nombre"): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Invalid("El $fieldName es requerido")
            name.length < 2 -> ValidationResult.Invalid("El $fieldName debe tener al menos 2 caracteres")
            name.length > 50 -> ValidationResult.Invalid("El $fieldName debe tener máximo 50 caracteres")
            !name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) -> ValidationResult.Invalid("Solo se permiten letras y espacios")
            name.trim() != name -> ValidationResult.Invalid("No debe empezar o terminar con espacios")
            name.contains(Regex("\\s{2,}")) -> ValidationResult.Invalid("No debe contener espacios múltiples")
            else -> ValidationResult.Valid
        }
    }

    fun validateTarjetaTitle(title: String): ValidationResult {
        return when {
            title.isBlank() -> ValidationResult.Invalid("El título es requerido")
            title.length < 5 -> ValidationResult.Invalid("El título debe tener al menos 5 caracteres")
            title.length > 100 -> ValidationResult.Invalid("El título debe tener máximo 100 caracteres")
            title.trim() != title -> ValidationResult.Invalid("No debe empezar o terminar con espacios")
            else -> ValidationResult.Valid
        }
    }

    fun validateTarjetaDescription(description: String, minLength: Int = 20): ValidationResult {
        return when {
            description.isBlank() -> ValidationResult.Invalid("La descripción es requerida")
            description.length < minLength -> ValidationResult.Invalid("La descripción debe tener al menos $minLength caracteres")
            description.length > 2000 -> ValidationResult.Invalid("La descripción debe tener máximo 2000 caracteres")
            description.trim() != description -> ValidationResult.Invalid("No debe empezar o terminar con espacios")
            else -> ValidationResult.Valid
        }
    }

    fun validateCategory(categoryId: Int?): ValidationResult {
        return when {
            categoryId == null -> ValidationResult.Invalid("La categoría es requerida")
            categoryId <= 0 -> ValidationResult.Invalid("Seleccione una categoría válida")
            else -> ValidationResult.Valid
        }
    }

    fun validateSector(sector: String): ValidationResult {
        return when {
            sector.isBlank() -> ValidationResult.Invalid("El sector es requerido")
            sector.length < 2 -> ValidationResult.Invalid("El sector debe tener al menos 2 caracteres")
            sector.length > 50 -> ValidationResult.Invalid("El sector debe tener máximo 50 caracteres")
            else -> ValidationResult.Valid
        }
    }

    fun validateMotivo(motivo: String): ValidationResult {
        return when {
            motivo.isBlank() -> ValidationResult.Invalid("El motivo es requerido")
            motivo.length < 10 -> ValidationResult.Invalid("El motivo debe tener al menos 10 caracteres")
            motivo.length > 500 -> ValidationResult.Invalid("El motivo debe tener máximo 500 caracteres")
            else -> ValidationResult.Valid
        }
    }

    fun validateDestinoFinal(destino: String): ValidationResult {
        return when {
            destino.isBlank() -> ValidationResult.Invalid("El destino final es requerido")
            destino.length < 10 -> ValidationResult.Invalid("El destino final debe tener al menos 10 caracteres")
            destino.length > 500 -> ValidationResult.Invalid("El destino final debe tener máximo 500 caracteres")
            else -> ValidationResult.Valid
        }
    }

    fun validateEstimatedDate(date: LocalDateTime?): ValidationResult {
        return when {
            date == null -> ValidationResult.Invalid("La fecha estimada es requerida")
            date.isBefore(LocalDateTime.now()) -> ValidationResult.Invalid("La fecha debe ser futura")
            date.isAfter(LocalDateTime.now().plusYears(2)) -> ValidationResult.Invalid("La fecha no puede ser mayor a 2 años")
            else -> ValidationResult.Valid
        }
    }

    fun validatePhone(phone: String?): ValidationResult {
        if (phone.isNullOrBlank()) return ValidationResult.Valid // Opcional
        
        val cleanPhone = phone.replace(Regex("[\\s()-]"), "")
        return when {
            !cleanPhone.matches(Regex("^\\+?[0-9]{8,15}$")) -> ValidationResult.Invalid("Formato de teléfono inválido")
            else -> ValidationResult.Valid
        }
    }

    fun validateEmployeeId(employeeId: String?): ValidationResult {
        if (employeeId.isNullOrBlank()) return ValidationResult.Valid // Opcional
        
        return when {
            employeeId.length < 3 -> ValidationResult.Invalid("El ID debe tener al menos 3 caracteres")
            employeeId.length > 20 -> ValidationResult.Invalid("El ID debe tener máximo 20 caracteres")
            !employeeId.matches(Regex("^[a-zA-Z0-9-]+$")) -> ValidationResult.Invalid("Solo letras, números y guiones")
            else -> ValidationResult.Valid
        }
    }

    fun validateCommentContent(content: String): ValidationResult {
        return when {
            content.isBlank() -> ValidationResult.Invalid("El comentario no puede estar vacío")
            content.length < 3 -> ValidationResult.Invalid("El comentario debe tener al menos 3 caracteres")
            content.length > 1000 -> ValidationResult.Invalid("El comentario debe tener máximo 1000 caracteres")
            content.trim() != content -> ValidationResult.Invalid("No debe empezar o terminar con espacios")
            else -> ValidationResult.Valid
        }
    }

    fun validateReportName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Invalid("El nombre del reporte es requerido")
            name.length < 3 -> ValidationResult.Invalid("El nombre debe tener al menos 3 caracteres")
            name.length > 100 -> ValidationResult.Invalid("El nombre debe tener máximo 100 caracteres")
            else -> ValidationResult.Valid
        }
    }

    fun validateMultiple(vararg validations: ValidationResult): ValidationResult {
        val firstError = validations.firstOrNull { it is ValidationResult.Invalid }
        return firstError ?: ValidationResult.Valid
    }
}

// Extensiones para facilitar el uso
fun String.validateEmail() = FormValidator().validateEmail(this)
fun String.validatePassword() = FormValidator().validatePassword(this)
fun String.validateUsername() = FormValidator().validateUsername(this)
fun String.validateName(fieldName: String = "nombre") = FormValidator().validateName(this, fieldName)
fun String.validateTarjetaTitle() = FormValidator().validateTarjetaTitle(this)
fun String.validateTarjetaDescription(minLength: Int = 20) = FormValidator().validateTarjetaDescription(this, minLength)
fun String.validateSector() = FormValidator().validateSector(this)
fun String.validateMotivo() = FormValidator().validateMotivo(this)
fun String.validateDestinoFinal() = FormValidator().validateDestinoFinal(this)
fun String?.validatePhone() = FormValidator().validatePhone(this)
fun String?.validateEmployeeId() = FormValidator().validateEmployeeId(this)
fun String.validateCommentContent() = FormValidator().validateCommentContent(this)
fun String.validateReportName() = FormValidator().validateReportName(this)
fun Int?.validateCategory() = FormValidator().validateCategory(this)
fun LocalDateTime?.validateEstimatedDate() = FormValidator().validateEstimatedDate(this)