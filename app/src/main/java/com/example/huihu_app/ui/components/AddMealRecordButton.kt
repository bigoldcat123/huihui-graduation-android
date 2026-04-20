package com.example.huihu_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealRecordButton(
    onCreateRecord: (String, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    ExtendedFloatingActionButton(
        onClick = { showSheet = true },
        modifier = modifier
    ) {
        Icon(Icons.Default.Fastfood, contentDescription = null)
        Text("添加饮食记录")
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            AddMealRecordContent(
                onDismiss = { showSheet = false },
                onCreateRecord = { mealType, calories ->
                    onCreateRecord(mealType, calories)
                    showSheet = false
                }
            )
        }
    }
}

@Composable
fun AddMealRecordContent(
    onDismiss: () -> Unit,
    onCreateRecord: (String, Double) -> Unit
) {
    val mealTypes = listOf("breakfast", "lunch", "dinner", "snack")
    var selectedMealType by remember { mutableStateOf("lunch") }
    var caloriesText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "添加饮食记录",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "选择餐次",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )


        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            mealTypes.forEach { mealType ->
                val isSelected = selectedMealType == mealType
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedMealType = mealType },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                ) {
                    Text(
                        text = when (mealType) {
                            "breakfast" -> "早餐"
                            "lunch" -> "午餐"
                            "dinner" -> "晚餐"
                            "snack" -> "零食"
                            else -> mealType
                        },
                        modifier = Modifier.padding(16.dp),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = caloriesText,
            onValueChange = { caloriesText = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("卡路里 (kcal)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val calories = caloriesText.toDoubleOrNull() ?: 0.0
                if (calories > 0 && selectedMealType.isNotEmpty()) {
                    onCreateRecord(selectedMealType, calories)
                }
            },
            enabled = caloriesText.toDoubleOrNull() != null && caloriesText.toDoubleOrNull()!! > 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
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