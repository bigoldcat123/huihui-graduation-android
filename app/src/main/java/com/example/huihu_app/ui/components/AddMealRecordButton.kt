package com.example.huihu_app.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.huihu_app.data.repository.MealRecordRepository
import com.example.huihu_app.data.repository.TopicRepository
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealRecordButton(
    onCreateRecord: (String, Double) -> Unit,
    topicRepository: TopicRepository? = null,
    mealRecordRepository: MealRecordRepository? = null,
    modifier: Modifier = Modifier
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
            sheetState = sheetState,
            modifier = Modifier.imePadding()
        ) {
            AddMealRecordContent(
                onDismiss = { showSheet = false },
                onCreateRecord = { mealType, calories ->
                    onCreateRecord(mealType, calories)
                    showSheet = false
                },
                topicRepository = topicRepository,
                mealRecordRepository = mealRecordRepository
            )
        }
    }
}

@Composable
fun AddMealRecordContent(
    onDismiss: () -> Unit,
    onCreateRecord: (String, Double) -> Unit,
    topicRepository: TopicRepository? = null,
    mealRecordRepository: MealRecordRepository? = null
) {
    var inputMode by remember { mutableStateOf("manual") }
    var selectedMealType by remember { mutableStateOf("lunch") }
    var caloriesText by remember { mutableStateOf("") }
    var isRecognizing by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var recognizedKcal by remember { mutableStateOf<Double?>(null) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            isRecognizing = true
            errorMsg = null
            scope.launch {
                val part = uriToPart(context.contentResolver, uri)
                if (part == null) {
                    errorMsg = "读取图片失败"
                    isRecognizing = false
                    return@launch
                }
                val response = topicRepository?.uploadImages(listOf(part))
                if (response?.isSuccess() == true) {
                    val imageUrl = response.data?.firstOrNull()
                    if (imageUrl != null) {
                        val encodedUrl = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString())
                        try {
                            val recognizeResponse = mealRecordRepository?.recognizeFood("$encodedUrl")
                            if (recognizeResponse?.isSuccess() == true) {
                                recognizedKcal = recognizeResponse.data
                                caloriesText = recognizedKcal?.toInt()?.toString() ?: ""
                            } else {
                                errorMsg = recognizeResponse?.message ?: "识别失败"
                            }
                        } catch (e: Exception) {
                            errorMsg = e.message ?: "识别失败"
                        }
                    }
                } else {
                    errorMsg = response?.message ?: "上传图片失败"
                }
                isRecognizing = false
            }
        }
    }

    LazyColumn (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text(
            text = "添加饮食记录",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        ) }

        item { InputModeSelector(
            inputMode = inputMode,
            onInputModeChange = { inputMode = it }
        ) }

        item { if (inputMode == "manual") {
            ManualInputSection(
                selectedMealType = selectedMealType,
                onMealTypeChange = { selectedMealType = it },
                caloriesText = caloriesText,
                onCaloriesChange = { caloriesText = it }
            )
        } else {
            ImageRecognitionSection(
                selectedImageUri = selectedImageUri,
                isRecognizing = isRecognizing,
                errorMsg = errorMsg,
                selectedMealType = selectedMealType,
                onMealTypeChange = { selectedMealType = it },
                caloriesText = caloriesText,
                onCaloriesChange = { caloriesText = it },
                onPickImage = {
                    imagePicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onRemoveImage = {
                    selectedImageUri = null
                    recognizedKcal = null
                    caloriesText = ""
                }
            )
        } }

        val calories = caloriesText.toDoubleOrNull() ?: 0.0
        item {
            Button(
                onClick = {
                    if (calories > 0) {
                        onCreateRecord(selectedMealType, calories)
                    }
                },
                enabled = calories > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("创建记录")
            } }

//        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
