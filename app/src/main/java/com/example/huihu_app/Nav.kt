package com.example.huihu_app

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Nav {
    @Serializable
    object Login: Nav, NavKey
    @Serializable
    object Register: Nav, NavKey
    @Serializable
    object CreateTopic: Nav, NavKey
    @Serializable
    data class WriteComment(val commentToId: Int): Nav, NavKey
    @Serializable
    object NewPerson: Nav, NavKey
    @Serializable
    object Home: Nav, NavKey
    @Serializable
    object Splash: Nav, NavKey
}
