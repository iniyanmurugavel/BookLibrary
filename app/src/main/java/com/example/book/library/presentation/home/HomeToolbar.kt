package com.example.book.library.presentation.home

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Toolbar(
    title: String,
    onLogoutClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title,style = MaterialTheme.typography.h6, color = Color.White)
        },
        actions = {
            IconButton(onClick = { onLogoutClick() }) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White
    )
}