package edu.utp.taskflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.utp.taskflow.data.repository.AuthRepository
import edu.utp.taskflow.data.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    val currentUserId: StateFlow<Int?> = repository.currentUserId
    val currentUsername: StateFlow<String?> = repository.currentUsername

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun login(username: String, password: String) = submit {
        repository.login(username, password)
    }

    fun register(username: String, password: String, confirm: String) {
        if (password != confirm) {
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }
        submit { repository.register(username, password) }
    }

    fun logout() {
        repository.logout()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun submit(action: suspend () -> AuthResult) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            when (val result = action()) {
                is AuthResult.Success -> Unit
                is AuthResult.Error -> _errorMessage.value = result.message
            }
            _isLoading.value = false
        }
    }
}

class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(AuthViewModel::class.java))
        return AuthViewModel(repository) as T
    }
}
