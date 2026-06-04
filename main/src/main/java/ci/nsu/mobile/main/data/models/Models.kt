package ci.nsu.mobile.main.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("groupId") val groupId: Long,
    @SerialName("groupName") val groupName: String
)

@Serializable
data class PersonInputDto(
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("middleName") val middleName: String? = null,
    @SerialName("birthDate") val birthDate: String, // format: "YYYY-MM-DD"
    @SerialName("gender") val gender: String,
    @SerialName("groupId") val groupId: Long
)

@Serializable
data class RegistrationRequestDto(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String,
    @SerialName("email") val email: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("roleId") val roleId: Long = 1,
    @SerialName("authAllowed") val authAllowed: Boolean = true,
    @SerialName("person") val person: PersonInputDto
)

@Serializable
data class UserLoginRequestDto(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String
)

@Serializable
data class AuthResponseDto(
    @SerialName("token") val token: String
)

@Serializable
data class UserDto(
    @SerialName("userId") val userId: Long,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String? = null,
    @SerialName("phoneNumber") val phoneNumber: String? = null,
    @SerialName("roleId") val roleId: Long? = null,
    @SerialName("authAllowed") val authAllowed: Boolean? = null,
    @SerialName("personId") val personId: Long? = null,
    @SerialName("createdDate") val createdDate: String? = null,
    @SerialName("lastLoginDate") val lastLoginDate: String? = null
)