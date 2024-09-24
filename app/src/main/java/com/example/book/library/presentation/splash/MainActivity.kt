package com.example.book.library.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.book.library.presentation.authentication.AuthenticationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewmodel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                when (viewmodel.isLoggedIn.value) {
                    true -> true
                    false -> {
                        startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
                        finish()
                        false
                    }
                    null -> true
                }
            }
        }
    }
}