package ci.nsu.mobile.main.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("groupId")
    val id: Int,
    @SerialName("groupName")
    val name: String
)

@Serializable
data class PersonDto(
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("middleName") val middleName: String? = null,
    @SerialName("birthDate") val birthDate: String, // Формат ГГГГ-ММ-ДД
    @SerialName("gender") val gender: String,      // MALE / FEMALE
    @SerialName("groupId") val groupId: Int
)

@Serializable
data class RegisterRequest(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String,
    @SerialName("email") val email: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("roleId") val roleId: Int = 1,
    @SerialName("authAllowed") val authAllowed: Boolean = true,
    @SerialName("person") val person: PersonDto
)

@Serializable
data class LoginRequest(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String
)

@Serializable
data class UserDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String? = null,
    @SerialName("phoneNumber") val phoneNumber: String? = null,
    @SerialName("token") val token: String? = null, // Токен, приходящий при авторизации
    @SerialName("person") val person: PersonDto? = null
)