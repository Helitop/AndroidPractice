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
    // Получаем текущее состояние из ViewModel
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Отображение значения счетчика
        Text(text = "Значение счетчика:", fontSize = 18.sp)
        Text(
            text = "${state.count}",
            fontSize = 64.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // 2. Колонка с 3 кнопками (как в задании)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { vm.increment() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Плюс (+1)", fontSize = 18.sp)
            }

            Button(
                onClick = { vm.decrement() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Минус (-1)", fontSize = 18.sp)
            }

            Button(
                onClick = { vm.reset() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(text = "Сбросить", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Список истории последних 5 действий
        Text(text = "История (последние 5):", fontSize = 16.sp)
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(state.history) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(text = item, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}
