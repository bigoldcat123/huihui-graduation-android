package com.example.huihu_app.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huihu_app.ui.AppViewModelProvider
import com.example.huihu_app.ui.components.FoodLoadingCard
import com.example.huihu_app.ui.components.TodayFoodActionBar
import com.example.huihu_app.ui.components.TodayFoodCard
import com.example.huihu_app.ui.components.TodayFoodNextAction
import com.example.huihu_app.ui.viewModel.FoodRecommendationViewModel

data class MockComment(
    val content: String,
    val create_time: String,
    val number_of_thumbs: Int
)

private val mockComments = listOf(
    MockComment(
        content = "这家店的味道真的不错！推荐大家来试试。",
        create_time = "2026-04-14 10:30",
        number_of_thumbs = 12
    ),
    MockComment(
        content = "价格实惠，分量也很足，下次还会再来。",
        create_time = "2026-04-13 18:45",
        number_of_thumbs = 8
    ),
    MockComment(
        content = "环境很好，适合朋友聚会。",
        create_time = "2026-04-12 12:20",
        number_of_thumbs = 5
    ),
    MockComment(
        content = "服务态度不错，上菜速度也快。",
        create_time = "2026-04-11 20:00",
        number_of_thumbs = 3
    ),
    MockComment(
        content = "味道正宗，很满意的一次用餐体验！",
        create_time = "2026-04-10 19:15",
        number_of_thumbs = 15
    )
)

@Composable
fun FoodRecommendationScreen(
    token: String,
    isRandomMode: Boolean,
    onFoodClick: (Int) -> Unit = {},
    viewModel: FoodRecommendationViewModel = viewModel(factory = AppViewModelProvider.FACTORY)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(token) {
        viewModel.load(token)
    }
    LaunchedEffect(isRandomMode) {
        viewModel.onRandomModeChanged(isRandomMode)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Food section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                when {
                    uiState.isLoading && uiState.currentFood == null -> {
                        FoodLoadingCard(modifier = Modifier.fillMaxWidth())
                    }

                    uiState.error != null && uiState.currentFood == null -> {
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(onClick = { viewModel.retry() }) {
                            Text("重试")
                        }
                    }

                    uiState.currentFood == null -> {
                        Text(
                            text = "当前暂无推荐。",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(onClick = { viewModel.load(token = token, force = true) }) {
                            Text("刷新推荐")
                        }
                    }

                    else -> {
                        val currentFood = uiState.currentFood!!
                        val isAccepted = uiState.acceptedFoodId == currentFood.id
                        TodayFoodCard(
                            food = currentFood,
                            isCelebrating = isAccepted,
                            onFoodClick = onFoodClick,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (isAccepted) {
                            TodayFoodNextAction(
                                onNextFood = { viewModel.nextFood() },
                                enabled = !uiState.isLoading,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedButton(
                                onClick = { /* TODO: add comment */ },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("添加评论")
                            }
                        } else {
                            TodayFoodActionBar(
                                onThatsIt = { viewModel.onThatsIt() },
                                onChangeIt = { viewModel.onChangeIt() },
                                onDontLikeIt = { viewModel.onDontLikeIt() },
                                enabled = !uiState.isLoading,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                if (uiState.feedbackMessage != null) {
                    Text(
                        text = uiState.feedbackMessage!!,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (uiState.error != null && uiState.currentFood != null) {
                    Text(
                        text = "同步网络异常：${uiState.error}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Comments section
        if (uiState.currentFood != null) {
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "评论",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(mockComments) { comment ->
                CommentItem(comment = comment)
            }
        }
    }
}

@Composable
private fun CommentItem(comment: MockComment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.create_time,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${comment.number_of_thumbs}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
