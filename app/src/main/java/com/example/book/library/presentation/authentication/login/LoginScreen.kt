package com.example.book.library.presentation.authentication.login

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.book.library.data.UserEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToRegistration: () -> Unit,
    navigateToHome: () -> Unit
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    LaunchedEffect(showPassword) {
        if (showPassword) {
            delay(2000)
            showPassword = false
        }
    }
    LaunchedEffect(Unit) {
        viewModel.uiEffect.onEach { effect ->
            when (effect) {
                is LoginEffect.NavigateToHome -> {
                    navigateToHome()
                }
            }
        }.collect()
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = email, onValueChange = {
            email = it
        }, label = {
            Text(text = "Email")
        })

        OutlinedTextField(
            value = password,
            visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            onValueChange = {
                password = it
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Add else Icons.Filled.Email,
                        contentDescription = null
                    )
                }
            },
            label = {
                Text(text = "Password")
            },
        )
        if(uiState.isUserNotFound != null) {
            Text(text = "User Not Found!, Please Register", modifier = Modifier.padding(vertical = 6.dp))
        } else if (uiState.incorrectCredentials != null) {
            Text(text = "Incorrect Credentials!", modifier = Modifier.padding(vertical = 6.dp))
        }
        Button(onClick = {
            viewModel.verifyCredentials(UserEntity(mail = email, password, ""))
        }, content = {
            Text(text = "Login!")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp, vertical = 16.dp)
        )
        Text(
            text = "New User? , Register",
            style = TextStyle.Default,
            modifier = Modifier.clickable {
                navigateToRegistration()
            })
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            navigateToRegistration = {}, modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White
                ),
            navigateToHome = {}
        )
    }
}