package com.cocido.formokaizen.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocido.formokaizen.domain.entities.AuthToken
import com.cocido.formokaizen.domain.entities.RegisterRequest
import com.cocido.formokaizen.domain.entities.User
import com.cocido.formokaizen.domain.usecases.auth.*
import com.cocido.formokaizen.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val authRepository: com.cocido.formokaizen.domain.repository.AuthRepository
) : ViewModel() {
    
    private val _loginState = MutableStateFlow<Resource<AuthToken>?>(null)
    val loginState: StateFlow<Resource<AuthToken>?> = _loginState.asStateFlow()
    
    private val _registerState = MutableStateFlow<Resource<AuthToken>?>(null)
    val registerState: StateFlow<Resource<AuthToken>?> = _registerState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<Resource<User>?>(null)
    val currentUser: StateFlow<Resource<User>?> = _currentUser.asStateFlow()
    
    private val _logoutState = MutableStateFlow<Resource<Unit>?>(null)
    val logoutState: StateFlow<Resource<Unit>?> = _logoutState.asStateFlow()
    
    // Estado combinado para la UI de auth
    val authState: StateFlow<Resource<AuthToken>?> = _loginState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // UI States
    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState: StateFlow<LoginFormState> = _loginFormState.asStateFlow()
    
    private val _registerFormState = MutableStateFlow(RegisterFormState())
    val registerFormState: StateFlow<RegisterFormState> = _registerFormState.asStateFlow()
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).collect { resource ->
                _loginState.value = resource
                _isLoading.value = resource is Resource.Loading
            }
        }
    }
    
    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            registerUseCase(request).collect { resource ->
                _registerState.value = resource
                _isLoading.value = resource is Resource.Loading
            }
        }
    }
    
    fun getCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { resource ->
                _currentUser.value = resource
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            logoutUseCase().collect { resource ->
                _logoutState.value = resource
                _isLoading.value = resource is Resource.Loading
            }
        }
    }
    
    fun updateLoginForm(email: String, password: String) {
        _loginFormState.value = _loginFormState.value.copy(
            email = email,
            password = password,
            isValid = email.isNotBlank() && password.isNotBlank()
        )
    }
    
    fun updateRegisterForm(
        username: String = _registerFormState.value.username,
        email: String = _registerFormState.value.email,
        password: String = _registerFormState.value.password,
        passwordConfirm: String = _registerFormState.value.passwordConfirm,
        firstName: String = _registerFormState.value.firstName,
        lastName: String = _registerFormState.value.lastName,
        employeeId: String? = _registerFormState.value.employeeId,
        phone: String? = _registerFormState.value.phone,
        department: String? = _registerFormState.value.department,
        position: String? = _registerFormState.value.position
    ) {
        val isValid = username.isNotBlank() && 
                     email.isNotBlank() && 
                     password.isNotBlank() && 
                     password == passwordConfirm &&
                     firstName.isNotBlank() &&
                     lastName.isNotBlank()
        
        _registerFormState.value = RegisterFormState(
            username = username,
            email = email,
            password = password,
            passwordConfirm = passwordConfirm,
            firstName = firstName,
            lastName = lastName,
            employeeId = employeeId,
            phone = phone,
            department = department,
            position = position,
            isValid = isValid
        )
    }
    
    fun clearLoginState() {
        _loginState.value = null
    }
    
    fun clearRegisterState() {
        _registerState.value = null
    }
    
    fun clearLogoutState() {
        _logoutState.value = null
    }
    
    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }
}

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val isValid: Boolean = false
)

data class RegisterFormState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val employeeId: String? = null,
    val phone: String? = null,
    val department: String? = null,
    val position: String? = null,
    val isValid: Boolean = false
)