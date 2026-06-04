package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.viewmodel.AppScreen
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.registerState.collectAsState()

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var middleName by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("2000-01-01") }
    var gender by rememberSaveable { mutableStateOf("MALE") }
    var selectedGroupId by rememberSaveable { mutableStateOf<Int?>(null) }
    var selectedGroupName by rememberSaveable { mutableStateOf("") }

    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    var expandedGroupDropdown by remember { mutableStateOf(false) }
    var expandedGenderDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Фамилия *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Имя *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = middleName,
            onValueChange = { middleName = it },
            label = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Дата рождения (ГГГГ-ММ-ДД) *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Выбор Пола
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = if (gender == "MALE") "Мужской" else "Женский",
                onValueChange = {},
                readOnly = true,
                label = { Text("Пол *") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expandedGenderDropdown = true }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedGenderDropdown = true }
            )
            DropdownMenu(
                expanded = expandedGenderDropdown,
                onDismissRequest = { expandedGenderDropdown = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(
                    text = { Text("Мужской") },
                    onClick = {
                        gender = "MALE"
                        expandedGenderDropdown = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Женский") },
                    onClick = {
                        gender = "FEMALE"
                        expandedGenderDropdown = false
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Выбор Группы (загруженной с сервера)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedGroupName.ifBlank { "Выберите группу *" },
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа *") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expandedGroupDropdown = true }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedGroupDropdown = true }
            )
            DropdownMenu(
                expanded = expandedGroupDropdown,
                onDismissRequest = { expandedGroupDropdown = false }
            ) {
                if (state.groups.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Группы не загружены (нажмите для обновления)") },
                        onClick = {
                            viewModel.loadGroups()
                            expandedGroupDropdown = false
                        }
                    )
                } else {
                    state.groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                selectedGroupId = group.id
                                selectedGroupName = group.name
                                expandedGroupDropdown = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль *") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Телефон *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    viewModel.register(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        birthDate = birthDate,
                        gender = gender,
                        groupId = selectedGroupId,
                        loginVal = login,
                        passwordVal = password,
                        emailVal = email,
                        phoneVal = phone
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зарегистрироваться")
            }
        }

        state.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { viewModel.navigateTo(AppScreen.Login) }
        ) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}