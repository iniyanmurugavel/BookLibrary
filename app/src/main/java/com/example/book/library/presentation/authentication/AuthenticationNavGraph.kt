package com.example.book.library.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.book.library.presentation.authentication.signUp.SignUpScreen
import kotlinx.serialization.Serializable

@Composable
fun AuthenticationNavGraph(
    modifier: Modifier = Modifier, navigateToHome: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AuthenticationScreens.SignUp
    ) {
        composable<AuthenticationScreens.Login> {

        }

        composable<AuthenticationScreens.SignUp> {
            SignUpScreen(navigateToLogin = {
                navController.navigate(AuthenticationScreens.Login)
            }, navigateToHome = {
                navigateToHome()
            })
        }
    }
}

@Serializable
sealed class AuthenticationScreens {

    @Serializable
    data object Login : AuthenticationScreens()

    @Serializable
    data object SignUp : AuthenticationScreens()
}