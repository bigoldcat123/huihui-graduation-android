package com.example.huihu_app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.huihu_app.ui.viewModel.AuthUiState

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    uiState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit
) {

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Card(modifier) {
        Column {
            OutlinedTextField(
                value = userName,
                onValueChange = {userName = it},
                label = { Text("用户名") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = { Text("密码") }
            )
            Button(onClick = {onLogin(userName,password)}, enabled = !uiState.isLoading) {
                Text("登录")
            }
            TextButton(onClick = onRegister, enabled = !uiState.isLoading) {
                Text("创建账号")
            }
            if (uiState.error != null) {
                Text(uiState.error)
            }
        }
    }
}

@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    uiState: AuthUiState,
    onRegister: (String, String, String) -> Unit,
    onLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Card(modifier) {
        Column {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("邮箱") }
            )
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("用户名") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("密码") }
            )
            Button(
                onClick = { onRegister(email, userName, password) },
                enabled = !uiState.isLoading
            ) {
                Text("注册")
            }
            TextButton(onClick = onLogin, enabled = !uiState.isLoading) {
                Text("已有账号？去登录")
            }
            if (uiState.error != null) {
                Text(uiState.error)
            }
        }
    }
}
