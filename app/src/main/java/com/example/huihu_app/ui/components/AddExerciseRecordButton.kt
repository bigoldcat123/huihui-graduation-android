package com.example.huihu_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.huihu_app.data.model.ExerciseType

data class ExerciseRecordState(
    val isLoading: Boolean = false,
    val exerciseTypes: List<ExerciseType> = emptyList(),
    val selectedExerciseTypeId: Int? = null,
    val durationMinutes: Int = 0,
    val userWeight: Double = 0.0,
    val error: String? = null,
    val isSaving: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseRecordButton(
    state: ExerciseRecordState,
    onSelectExerciseType: (Int) -> Unit,
    onUpdateDuration: (Int) -> Unit,
    onCreateExerciseRecord: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ExtendedFloatingActionButton(
        onClick = { showSheet = true },
        modifier = modifier
    ) {
        Icon(Icons.AutoMirrored.Filled.DirectionsRun, contentDescription = null)
        Text("添加运动记录")
    }

    if (showSheet) {
        ModalBottomSheet(
            modifier = Modifier.imePadding(),
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            AddExerciseRecordContent(
                state = state,
                onSelectExerciseType = onSelectExerciseType,
                onUpdateDuration = onUpdateDuration,
                onCreateExerciseRecord = onCreateExerciseRecord,
                onDismiss = { showSheet = false }
            )
        }
    }
}

@Composable
fun AddExerciseRecordContent(
    state: ExerciseRecordState,
    onSelectExerciseType: (Int) -> Unit,
    onUpdateDuration: (Int) -> Unit,
    onCreateExerciseRecord: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "添加运动记录",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "选择运动类型",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.exerciseTypes.chunked(2).forEach { rowTypes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowTypes.forEach { exerciseType ->
                            val isSelected = state.selectedExerciseTypeId == exerciseType.id
                            ExerciseTypeCard(
                                exerciseType = exerciseType,
                                isSelected = isSelected,
                                onClick = { onSelectExerciseType(exerciseType.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowTypes.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = if (state.durationMinutes > 0) state.durationMinutes.toString() else "",
            onValueChange = {
                val minutes = it.filter { c -> c.isDigit() }.toIntOrNull() ?: 0
                onUpdateDuration(minutes)
            },
            label = { Text("运动时长 (分钟)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "体重: ${state.userWeight} kg",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                onCreateExerciseRecord()
                onDismiss()
            },
            enabled = state.selectedExerciseTypeId != null &&
                    state.durationMinutes > 0 &&
                    !state.isSaving &&
                    state.userWeight > 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(4.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("创建记录")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseTypeCard(
    exerciseType: ExerciseType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.Bottom
        ) {
            Text(
                text = exerciseType.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = "MET: ${exerciseType.met_value}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}