package com.example.krushiscan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krushiscan.data.api.User
import com.example.krushiscan.data.repository.KrushiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User? = null, val message: String = "") : AuthState()
    data class Error(val message: String) : AuthState()
    data class OtpSent(val email: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    
    private val repository = KrushiRepository()
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = repository.login(email, password)
            result.onSuccess { response ->
                _authState.value = AuthState.Success(
                    user = response.user,
                    message = response.message
                )
            }.onFailure { error ->
                _authState.value = AuthState.Error(
                    error.message ?: "Login failed. Please try again."
                )
            }
        }
    }
    
    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = repository.signup(name, email, password)
            result.onSuccess { response ->
                _authState.value = AuthState.OtpSent(email)
            }.onFailure { error ->
                _authState.value = AuthState.Error(
                    error.message ?: "Signup failed. Please try again."
                )
            }
        }
    }
    
    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            val result = repository.verifyOtp(email, otp)
            result.onSuccess { response ->
                _authState.value = AuthState.Success(message = response.message)
            }.onFailure { error ->
                _authState.value = AuthState.Error(
                    error.message ?: "OTP verification failed. Please try again."
                )
            }
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
