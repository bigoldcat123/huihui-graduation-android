package com.example.huihu_app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.huihu_app.data.repository.TopicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ForumViewModel(
    topicRepository: TopicRepository
) : ViewModel() {
    val topics = topicRepository.pager().flow.cachedIn(viewModelScope)

    private val _refreshSignal = MutableStateFlow(0)
    val refreshSignal = _refreshSignal.asStateFlow()

    fun notifyTopicCreated() {
        _refreshSignal.update { it + 1 }
    }
}
