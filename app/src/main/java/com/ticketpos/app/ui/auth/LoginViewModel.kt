package com.ticketpos.app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticketpos.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _loginSuccess = MutableLiveData<Boolean>(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _failedLoginAttempts = MutableLiveData<Int>(0)
    val failedLoginAttempts: LiveData<Int> = _failedLoginAttempts

    private var currentFailedAttempts = 0

    fun login(username: String, password: String) {
        if (_isLoading.value == true) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""

                // Demo credentials
                if (username == "admin" && password == "admin123") {
                    // Simulate network delay
                    kotlinx.coroutines.delay(1000)
                    
                    // Save user session
                    userRepository.saveUserSession(username)
                    
                    _loginSuccess.value = true
                    currentFailedAttempts = 0
                    _failedLoginAttempts.value = currentFailedAttempts
                } else {
                    // Simulate network delay for failed login
                    kotlinx.coroutines.delay(1000)
                    
                    currentFailedAttempts++
                    _failedLoginAttempts.value = currentFailedAttempts
                    
                    if (currentFailedAttempts >= 3) {
                        _errorMessage.value = "Cuenta bloqueada por múltiples intentos fallidos"
                    } else {
                        _errorMessage.value = "Usuario o contraseña incorrectos"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    fun resetFailedAttempts() {
        currentFailedAttempts = 0
        _failedLoginAttempts.value = currentFailedAttempts
    }
}
