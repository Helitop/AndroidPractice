package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ci.nsu.mobile.main.data.network.NetworkClient
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.storage.TokenManager
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.ui.viewmodel.AppScreen
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {

    // Инициализируем ViewModel с использованием фабрики зависимостей
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(NetworkClient.apiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализируем TokenManager для SharedPreferences
        TokenManager.init(applicationContext)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentScreen by authViewModel.currentScreen.collectAsState()

                    // Простой декларативный роутинг на основе Compose State
                    when (currentScreen) {
                        is AppScreen.Login -> LoginScreen(viewModel = authViewModel)
                        is AppScreen.Register -> RegisterScreen(viewModel = authViewModel)
                        is AppScreen.Main -> MainScreen(viewModel = authViewModel)
                    }
                }
            }
        }
    }
}