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
                label = { Text("Username") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = { Text("Password") }
            )
            Button(onClick = {onLogin(userName,password)}, enabled = !uiState.isLoading) {
                Text("Login")
            }
            TextButton(onClick = onRegister, enabled = !uiState.isLoading) {
                Text("Create account")
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
                label = { Text("Email") }
            )
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Username") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )
            Button(
                onClick = { onRegister(email, userName, password) },
                enabled = !uiState.isLoading
            ) {
                Text("Register")
            }
            TextButton(onClick = onLogin, enabled = !uiState.isLoading) {
                Text("Already have an account? Login")
            }
            if (uiState.error != null) {
                Text(uiState.error)
            }
        }
    }
}
