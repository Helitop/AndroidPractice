package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 1. Класс, который хранит ВСЕ данные экрана в одной "коробке"
data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)

class CounterViewModel : ViewModel() {
    // 2. MutableStateFlow — это поток данных. Мы (внутри) можем его менять.
    private val _uiState = MutableStateFlow(CounterUiState())
    // 3. StateFlow — это то, что видит экран. Он может только "читать" данные.
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            // Добавляем запись в начало списка и оставляем только последние 5
            val newHistory = (listOf("+1 (итого: $newCount)") + currentState.history).take(5)
            // .copy создает копию состояния с измененными полями (Compose увидит это и перерисует экран)
            currentState.copy(count = newCount, history = newHistory)
        }
    }

    fun decrement() {
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val newHistory = (listOf("-1 (итого: $newCount)") + currentState.history).take(5)
            currentState.copy(count = newCount, history = newHistory)
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            val newHistory = (listOf("Сброс") + currentState.history).take(5)
            // Сбрасываем count в 0, обновляем историю
            currentState.copy(count = 0, history = newHistory)
        }
    }
}