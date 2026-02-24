package com.example.huihu_app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huihu_app.data.model.Topic
import com.example.huihu_app.data.repository.TopicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TopicManageUiState(
    val isLoading: Boolean = false,
    val topics: List<Topic> = emptyList(),
    val deletingTopicIds: Set<Int> = emptySet(),
    val error: String? = null
)

class TopicManageViewModel(
    private val topicRepository: TopicRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TopicManageUiState())
    val uiState = _uiState.asStateFlow()

    fun loadMyTopics(token: String) {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val response = topicRepository.myTopics(token)
            if (!response.isSuccess()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = response.message
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    topics = response.data.orEmpty(),
                    error = null
                )
            }
        }
    }

    fun deleteTopic(token: String, topicId: Int) {
        if (topicId in _uiState.value.deletingTopicIds) return
        _uiState.update { it.copy(deletingTopicIds = it.deletingTopicIds + topicId, error = null) }

        viewModelScope.launch {
            val response = topicRepository.deleteTopic(token = token, topicId = topicId)
            if (!response.isSuccess()) {
                _uiState.update {
                    it.copy(
                        deletingTopicIds = it.deletingTopicIds - topicId,
                        error = response.message
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    deletingTopicIds = it.deletingTopicIds - topicId,
                    topics = it.topics.filterNot { topic -> topic.id == topicId }
                )
            }
        }
    }
}
