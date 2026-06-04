package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(vm: CounterViewModel = viewModel()) {
    // "Подписываемся" на данные. Если данные в VM изменятся, экран сам перерисуется.
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Мой Счетчик", fontSize = 20.sp)

        // Отображаем число из состояния
        Text(
            text = "${state.count}",
            fontSize = 72.sp,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Кнопки управления
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка Минус (вес 1f нужен, чтобы кнопки были одинаковой ширины)
            Button(onClick = { vm.decrement() }, modifier = Modifier.weight(1f)) {
                Text("-", fontSize = 24.sp)
            }

            // Кнопка Сброс
            Button(onClick = { vm.reset() }, modifier = Modifier.weight(1f)) {
                Text("0", fontSize = 24.sp)
            }

            // Кнопка Плюс
            Button(onClick = { vm.increment() }, modifier = Modifier.weight(1f)) {
                Text("+", fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "История операций:", fontSize = 16.sp)

        // Список истории (аналог RecyclerView, но проще)
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.history) { action ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(text = action, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}