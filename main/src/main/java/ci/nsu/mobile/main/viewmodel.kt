package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// UiState - простой data class (наше состояние экрана)
data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)

class CounterViewModel : ViewModel() {
    // StateFlow - хранит состояние и сообщает UI об изменениях
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    // 1. Метод прибавить 1
    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            // Берем текущий список и оставляем только последние 4 (чтобы в сумме было 5)
            val newHistory = listOf("+1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    // 2. Метод убавить 1 (аналогично increment)
    fun decrement() {
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val newHistory = listOf("-1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    // 3. Метод сброса в ноль
    fun reset() {
        _uiState.update { currentState ->
            val newCount = 0
            val newHistory = listOf("Сброс (итого: 0)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }
}
