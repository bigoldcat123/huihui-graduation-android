package com.example.huihu_app

import com.example.huihu_app.data.model.CreateTopicRequest
import kotlinx.serialization.json.Json
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(
            Json.encodeToString(
                CreateTopicRequest(
                    "title",
                    "content",
                    emptyList(),
                    null,
                    "",
                    false
                )
            )
        );
    }
}