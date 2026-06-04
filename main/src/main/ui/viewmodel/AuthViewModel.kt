package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.PersonDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.storage.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AppScreen {
    object Login : AppScreen
    object Register : AppScreen
    object Main : AppScreen
}

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

data class RegisterState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val groups: List<GroupDto> = emptyList()
)

data class MainState(
    val users: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Login)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    init {
        // Если токен уже сохранен, сразу перенаправляем на главный экран
        if (!TokenManager.token.isNullOrEmpty()) {
            _currentScreen.value = AppScreen.Main
            loadUsers()
        }
        loadGroups()
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess { groupsList ->
                    _registerState.update { it.copy(groups = groupsList) }
                }
                .onFailure { error ->
                    _registerState.update { it.copy(error = "Ошибка загрузки групп: ${error.localizedMessage}") }
                }
        }
    }

    fun login(loginVal: String, passwordVal: String) {
        if (loginVal.isBlank() || passwordVal.isBlank()) {
            _loginState.update { it.copy(error = "Логин и пароль не должны быть пустыми") }
            return
        }

        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, error = null) }
            repository.login(loginVal, passwordVal)
                .onSuccess {
                    _loginState.update { it.copy(isLoading = false, success = true) }
                    _currentScreen.value = AppScreen.Main
                    loadUsers()
                }
                .onFailure { error ->
                    _loginState.update { it.copy(isLoading = false, error = "Ошибка авторизации: ${error.localizedMessage}") }
                }
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        middleName: String,
        birthDate: String,
        gender: String,
        groupId: Int?,
        loginVal: String,
        passwordVal: String,
        emailVal: String,
        phoneVal: String
    ) {
        if (firstName.isBlank() || lastName.isBlank() || loginVal.isBlank() || passwordVal.isBlank() || emailVal.isBlank() || phoneVal.isBlank() || groupId == null) {
            _registerState.update { it.copy(error = "Пожалуйста, заполните все поля") }
            return
        }

        viewModelScope.launch {
            _registerState.update { it.copy(isLoading = true, error = null) }
            val person = PersonDto(
                firstName = firstName,
                lastName = lastName,
                middleName = middleName.ifBlank { null },
                birthDate = birthDate,
                gender = gender,
                groupId = groupId
            )
            val request = RegisterRequest(
                login = loginVal,
                password = passwordVal,
                email = emailVal,
                phoneNumber = phoneVal,
                roleId = 1,
                authAllowed = true,
                person = person
            )

            repository.register(request)
                .onSuccess {
                    _registerState.update { it.copy(isLoading = false, success = true) }
                    _currentScreen.value = AppScreen.Login
                }
                .onFailure { error ->
                    _registerState.update { it.copy(isLoading = false, error = "Ошибка регистрации: ${error.localizedMessage}") }
                }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _mainState.update { it.copy(isLoading = true, error = null) }
            repository.getUsers()
                .onSuccess { usersList ->
                    _mainState.update { it.copy(users = usersList, isLoading = false) }
                }
                .onFailure { error ->
                    _mainState.update { it.copy(isLoading = false, error = "Ошибка: ${error.localizedMessage}") }
                }
        }
    }

    fun logout() {
        TokenManager.token = null
        _loginState.value = LoginState()
        _registerState.value = RegisterState()
        _mainState.value = MainState()
        _currentScreen.value = AppScreen.Login
    }
}

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}