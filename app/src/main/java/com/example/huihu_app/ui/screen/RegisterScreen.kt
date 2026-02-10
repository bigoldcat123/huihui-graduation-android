package com.example.huihu_app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huihu_app.ui.AppViewModelProvider
import com.example.huihu_app.ui.components.RegisterForm
import com.example.huihu_app.ui.viewModel.AuthViewModel

@Composable
fun RegisterScreen(
    onLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.FACTORY)
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold() { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            RegisterForm(
                uiState = uiState,
                onRegister = { email, username, password ->
                    viewModel.register(email, username, password)
                },
                onLogin = onLogin
            )
        }
    }
}
