package com.example.book.library.presentation.authentication.signUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.book.library.domain.model.Country
import com.example.book.library.presentation.authentication.common.validatePassword
import kotlinx.coroutines.delay


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit
) {
    val viewModel = hiltViewModel<SignUpViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    LaunchedEffect(showPassword) {
        if (showPassword) {
            delay(2000)
            showPassword = false
        }
    }
    uiState.countries?.let { countries ->
        val index = countries.indexOfFirst { it.country == uiState.selectedCountry?.country }
        val itemPosition = remember {
            mutableIntStateOf(if (index == -1) 0 else index)
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
                    errorMessage = validatePassword(password)
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Add else Icons.Filled.Email,
                            contentDescription = null
                        )
                    }
                }, label = {
                    Text(text = "Password")
                },
                isError = errorMessage.isNotBlank(),
                supportingText = {
                    if (errorMessage.isNotBlank()) {
                        Text(text = errorMessage, color = Color.Red)
                    }
                }
            )
            CountryDropDown(Modifier.padding(vertical = 6.dp), itemPosition, countries)
            Button(onClick = {
                viewModel.setUserLoggedIn(UserEntity(mail = email,password,country))
                navigateToHome()
            }, content = {
                Text(text = "Register!")
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 16.dp)
            )
            Text(
                text = "Already have account? , Login",
                style = TextStyle.Default,
                modifier = Modifier.clickable {
                    navigateToLogin()
                })
        }
    } ?: run {

    }
}

@Composable
fun CountryDropDown(
    modifier: Modifier = Modifier,
    itemPosition: MutableIntState,
    countries: List<Country>
) {

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                isDropDownExpanded.value = true
            }
        ) {
            Text(text = countries[itemPosition.intValue].country)
            Image(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "DropDown Icon"
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = {
                isDropDownExpanded.value = false
            }) {
            countries.forEachIndexed { index, country ->
                DropdownMenuItem(text = {
                    Text(text = country.country)
                },
                    onClick = {
                        isDropDownExpanded.value = false
                        itemPosition.intValue = index
                    })
            }
        }
    }
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        {},
        {}
    )
}