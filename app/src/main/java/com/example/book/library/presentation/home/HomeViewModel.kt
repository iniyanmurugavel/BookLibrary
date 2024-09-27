package com.example.book.library.presentation.home

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
class HomeViewModel @Inject constructor(
    private val localRepository: ILocalRepository,
) : ViewModel() {

    private val _uiEffect: Channel<HomeEffect> = Channel()
    val uiEffect: Flow<HomeEffect> = _uiEffect.receiveAsFlow()

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.setUserLoggedIn(false)
            _uiEffect.send(HomeEffect.NavigateToLogin)
        }
    }
}

sealed class HomeEffect {
    data object NavigateToLogin : HomeEffect()
}