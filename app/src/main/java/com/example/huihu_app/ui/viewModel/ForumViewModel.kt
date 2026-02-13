package com.example.huihu_app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.huihu_app.data.repository.TopicRepository

class ForumViewModel(
    topicRepository: TopicRepository
) : ViewModel() {
    val topics = topicRepository.pager().flow.cachedIn(viewModelScope)
}
