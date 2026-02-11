package com.example.huihu_app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huihu_app.data.model.Food
import com.example.huihu_app.ui.AppViewModelProvider
import com.example.huihu_app.ui.viewModel.NewPersonViewModel

@Composable
fun NewPersonScreen(
    token: String,
    viewModel: NewPersonViewModel = viewModel(factory = AppViewModelProvider.FACTORY)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(token) {
        viewModel.start(token)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Round ${uiState.currentRound}/2")

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            if (uiState.error != null) {
                Text(uiState.error!!)
                Button(onClick = { viewModel.retry() }) {
                    Text("Retry")
                }
            }

            if (!uiState.isLoading) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(uiState.foods, key = { it.id }) { food ->
                        FoodItem(
                            food = food,
                            checked = uiState.selectedCurrentRound.contains(food.id),
                            onToggle = { viewModel.toggleFoodSelection(food.id) }
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.submitCurrentRound() },
                enabled = !uiState.isLoading && uiState.selectedCurrentRound.isNotEmpty() && !uiState.isCompleted,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.currentRound < 2) "Next Round" else "Finish")
            }

            if (uiState.isCompleted) {
                Text("Onboarding completed")
            }
        }
    }
}

@Composable
private fun FoodItem(
    food: Food,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { onToggle() }
            )
            Column {
                Text(food.name)
                Text(food.description)
            }
        }
    }
}
