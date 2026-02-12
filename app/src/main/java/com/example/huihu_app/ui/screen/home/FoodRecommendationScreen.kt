package com.example.huihu_app.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huihu_app.ui.AppViewModelProvider
import com.example.huihu_app.ui.components.FoodLoadingCard
import com.example.huihu_app.ui.components.FoodReactionBar
import com.example.huihu_app.ui.components.SwipeFoodCard
import com.example.huihu_app.ui.viewModel.FoodRecommendationViewModel

@Composable
fun FoodRecommendationScreen(
    token: String,
    viewModel: FoodRecommendationViewModel = viewModel(factory = AppViewModelProvider.FACTORY)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(token) {
        viewModel.load(token)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Food Recommendation",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            if (uiState.pendingReactionCount > 0) {
                Text(
                    text = "Syncing feedback... (${uiState.pendingReactionCount})",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            when {
                uiState.isLoading && uiState.cards.isEmpty() -> {
                    FoodLoadingCard(modifier = Modifier.fillMaxWidth())
                }

                uiState.error != null && uiState.cards.isEmpty() -> {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(onClick = { viewModel.retry() }) {
                        Text("Retry")
                    }
                }

                uiState.cards.isEmpty() -> {
                    Text(
                        text = "No recommendations right now.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(onClick = { viewModel.load(token = token, force = true) }) {
                        Text("Refresh recommendations")
                    }
                }

                else -> {
                    val topCard = uiState.cards.first()
                    key(topCard.id) {
                        SwipeFoodCard(
                            food = topCard,
                            onSwipeRight = { viewModel.onLike() },
                            onSwipeLeft = { viewModel.onSkip() },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    FoodReactionBar(
                        onSkip = { viewModel.onSkip() },
                        onDislike = { viewModel.onDislike() },
                        onLike = { viewModel.onLike() },
                        enabled = !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "${uiState.cards.size} card(s) left in this batch",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (uiState.error != null && uiState.cards.isNotEmpty()) {
                Text(
                    text = "Network issue while updating feed: ${uiState.error}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (uiState.isLoading && uiState.cards.isNotEmpty()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
