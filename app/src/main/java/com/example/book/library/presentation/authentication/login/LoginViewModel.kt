package com.example.book.library.presentation.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book.library.data.UserEntity
import com.example.book.library.domain.ILocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localRepository: ILocalRepository,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEffect: Channel<LoginEffect> = Channel()
    val uiEffect: Flow<LoginEffect> = _uiEffect.receiveAsFlow()

    fun verifyCredentials(userEntity: UserEntity) {
        _uiState.value = _uiState.value.copy(
            incorrectCredentials = null,
            isUserNotFound = null,
        )
        viewModelScope.launch(Dispatchers.IO) {
            val localData = localRepository.getUserData()
            if((localData.mail == userEntity.mail) && (localData.password == userEntity.password)) {
                localRepository.setUserLoggedIn(true)
                localRepository.setUserData(userEntity)
                _uiEffect.send(LoginEffect.NavigateToHome)
            } else {
                if(localData.mail.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isUserNotFound = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        incorrectCredentials = true
                    )
                }
            }
        }
    }
}

data class LoginUiState(
    val isUserNotFound: Boolean? = null,
    val incorrectCredentials: Boolean? = null,
)

sealed class LoginEffect {
    data object NavigateToHome : LoginEffect()
}