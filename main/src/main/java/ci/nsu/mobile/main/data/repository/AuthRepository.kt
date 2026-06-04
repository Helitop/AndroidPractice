package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.ApiService
import ci.nsu.mobile.main.data.models.*
import ci.nsu.mobile.main.data.storage.TokenManager

class AuthRepository(private val apiService: ApiService) {

    suspend fun login(login: String, password: String): Result<AuthResponseDto> {
        return try {
            val response = apiService.login(UserLoginRequestDto(login, password))
            // Извлекаем токен из AuthResponseDto и сохраняем его
            TokenManager.token = response.token
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegistrationRequestDto): Result<Unit> {
        return try {
            apiService.register(registerRequest)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = apiService.getGroups()
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}