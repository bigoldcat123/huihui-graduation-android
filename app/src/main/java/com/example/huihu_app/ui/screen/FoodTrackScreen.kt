package com.example.huihu_app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun FoodTrackScreen() {
    val context = LocalContext.current
    Scaffold() {paddingValues ->
        Box(Modifier.padding(paddingValues)) {

        }
    }
}
