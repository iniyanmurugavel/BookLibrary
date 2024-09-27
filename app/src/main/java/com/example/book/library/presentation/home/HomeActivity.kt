package com.example.book.library.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.book.library.presentation.authentication.AuthenticationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val viewModel : HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.uiEffect.onEach { effect ->
                    when (effect) {
                        is HomeEffect.NavigateToLogin -> {
                            startActivity(Intent(this@HomeActivity, AuthenticationActivity::class.java))
                            finish()
                        }
                    }
                }.collect()
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Logout", modifier = Modifier.clickable {
                    viewModel.logout()
                })
            }
        }
    }
}