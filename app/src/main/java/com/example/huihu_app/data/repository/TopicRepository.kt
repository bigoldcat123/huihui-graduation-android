package com.example.huihu_app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.huihu_app.data.source.TopicSource

class TopicRepository(
    private val topicSource: TopicSource,
) {
    fun pager() = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 3,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TopicPagingSource(topicSource) }
    )
}
