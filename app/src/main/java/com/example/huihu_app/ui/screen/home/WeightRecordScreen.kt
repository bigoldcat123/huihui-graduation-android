package com.example.huihu_app.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huihu_app.ui.AppViewModelProvider
import com.example.huihu_app.ui.components.AddExerciseRecordButton
import com.example.huihu_app.ui.components.AddMealRecordButton
import com.example.huihu_app.ui.components.CalorieSummaryCard
import com.example.huihu_app.ui.components.RecordListSection
import com.example.huihu_app.ui.viewModel.WeightRecordViewModel

@Composable
fun WeightRecordScreen(
    token: String,
    onSetCalorieGoal: () -> Unit,
    viewModel: WeightRecordViewModel = viewModel(factory = AppViewModelProvider.FACTORY)
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(token) {
        viewModel.loadData(token)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        CalorieSummaryCard(
            calorieGoal = uiState.calorieGoal,
            totalCaloriesConsumed = uiState.totalCaloriesConsumed,
            totalCaloriesBurned = uiState.totalCaloriesBurned,
            onEditGoal = onSetCalorieGoal
        )

        ActionButtonsRow(
            onCreateMealRecord = { mealType, calories ->
                viewModel.createMealRecord(token, mealType, calories)
            },
            viewModel = viewModel,
            token = token
        )

        Spacer(modifier = Modifier.height(8.dp))

        RecordListSection(
            modifier = Modifier.weight(1f),
            selectedTab = selectedTab,
            exerciseRecords = uiState.exerciseRecords,
            mealRecords = uiState.mealRecords,
            onTabSelected = { selectedTab = it },
            onSetCalorieGoal = onSetCalorieGoal,
            calorieGoal = uiState.calorieGoal
        )

        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ActionButtonsRow(
    onCreateMealRecord: (String, Double) -> Unit,
    viewModel: WeightRecordViewModel,
    token: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        AddExerciseRecordButton(
            viewModel = viewModel,
            token = token,
            modifier = Modifier.weight(1f)
        )
        AddMealRecordButton(
            modifier = Modifier.weight(1f),
            onCreateRecord = onCreateMealRecord,
            topicRepository = viewModel.topic,
            mealRecordRepository = viewModel.mealRecordRepository,
            imageRepository = viewModel.imageRepository
        )
    }
}
