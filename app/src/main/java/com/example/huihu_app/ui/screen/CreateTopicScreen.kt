package com.example.huihu_app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huihu_app.ui.AppViewModelProvider
import com.example.huihu_app.ui.components.TopicImageUploadSection
import com.example.huihu_app.ui.viewModel.CreateTopicViewModel

@Composable
fun CreateTopicScreen(
    token: String,
    commentToId: Int? = null,
    screenTitle: String = "Create Topic",
    submitButtonText: String = "Create Topic",
    onCreated: () -> Unit,
    viewModel: CreateTopicViewModel = viewModel(factory = AppViewModelProvider.FACTORY)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.created) {
        if (uiState.created) {
            viewModel.clearCreatedFlag()
            onCreated()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = screenTitle,
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::updateTitle,
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.content,
                onValueChange = viewModel::updateContent,
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                minLines = 5
            )

            TopicImageUploadSection(
                selectedImages = uiState.selectedImages,
                onPickImages = { uris ->
                    viewModel.onImagesPicked(context.contentResolver, uris)
                },
                onRemoveImage = viewModel::removeImage,
                isUploadingImages = uiState.isUploadingImages,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = { viewModel.submit(token = token, commentToId = commentToId) },
                enabled = !uiState.isSubmitting && !uiState.isUploadingImages,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(submitButtonText)
                }
            }
        }
    }
}
