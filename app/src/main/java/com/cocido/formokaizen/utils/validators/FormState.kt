package com.cocido.formokaizen.utils.validators

data class FieldState(
    val value: String = "",
    val error: String? = null,
    val isValid: Boolean = true,
    val isTouched: Boolean = false
) {
    fun validate(validator: (String) -> ValidationResult): FieldState {
        val result = validator(value)
        return copy(
            error = when (result) {
                is ValidationResult.Invalid -> result.message
                ValidationResult.Valid -> null
            },
            isValid = result is ValidationResult.Valid,
            isTouched = true
        )
    }

    fun updateValue(newValue: String): FieldState {
        return copy(value = newValue)
    }

    fun touch(): FieldState {
        return copy(isTouched = true)
    }

    fun reset(): FieldState {
        return FieldState()
    }
}

data class CreateTarjetaFormState(
    val title: FieldState = FieldState(),
    val description: FieldState = FieldState(),
    val category: FieldState = FieldState(),
    val sector: FieldState = FieldState(),
    val motivo: FieldState = FieldState(),
    val destinoFinal: FieldState = FieldState(),
    val estimatedDate: FieldState = FieldState(),
    val assignedTo: FieldState = FieldState(),
    val workArea: FieldState = FieldState()
) {
    val isValid: Boolean
        get() = listOf(title, description, category, sector, motivo, destinoFinal)
            .all { it.isValid }

    val hasErrors: Boolean
        get() = listOf(title, description, category, sector, motivo, destinoFinal, estimatedDate, assignedTo, workArea)
            .any { it.error != null }

    fun validateAll(): CreateTarjetaFormState {
        return copy(
            title = title.validate { it.validateTarjetaTitle() },
            description = description.validate { it.validateTarjetaDescription() },
            category = category.validate { 
                val categoryId = it.toIntOrNull()
                categoryId.validateCategory()
            },
            sector = sector.validate { it.validateSector() },
            motivo = motivo.validate { it.validateMotivo() },
            destinoFinal = destinoFinal.validate { it.validateDestinoFinal() }
        )
    }

    fun reset(): CreateTarjetaFormState {
        return CreateTarjetaFormState()
    }
}

data class LoginFormState(
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState()
) {
    val isValid: Boolean
        get() = email.isValid && password.isValid && 
               email.value.isNotBlank() && password.value.isNotBlank()

    fun validateAll(): LoginFormState {
        return copy(
            email = email.validate { it.validateEmail() },
            password = password.validate { 
                if (it.isBlank()) ValidationResult.Invalid("La contraseña es requerida")
                else ValidationResult.Valid
            }
        )
    }
}

data class RegisterFormState(
    val username: FieldState = FieldState(),
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val confirmPassword: FieldState = FieldState(),
    val firstName: FieldState = FieldState(),
    val lastName: FieldState = FieldState(),
    val employeeId: FieldState = FieldState(),
    val phone: FieldState = FieldState(),
    val department: FieldState = FieldState(),
    val position: FieldState = FieldState()
) {
    val isValid: Boolean
        get() = listOf(username, email, password, confirmPassword, firstName, lastName)
            .all { it.isValid } && password.value == confirmPassword.value

    fun validateAll(): RegisterFormState {
        val validator = FormValidator()
        return copy(
            username = username.validate { it.validateUsername() },
            email = email.validate { it.validateEmail() },
            password = password.validate { it.validatePassword() },
            confirmPassword = confirmPassword.validate { 
                validator.validateConfirmPassword(password.value, it)
            },
            firstName = firstName.validate { it.validateName("nombre") },
            lastName = lastName.validate { it.validateName("apellido") },
            employeeId = employeeId.validate { it.validateEmployeeId() },
            phone = phone.validate { it.validatePhone() }
        )
    }
}

data class CommentFormState(
    val content: FieldState = FieldState()
) {
    val isValid: Boolean
        get() = content.isValid && content.value.isNotBlank()

    fun validateAll(): CommentFormState {
        return copy(
            content = content.validate { it.validateCommentContent() }
        )
    }
}