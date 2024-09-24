package com.example.book.library.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book.library.domain.ILocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepository: ILocalRepository
) : ViewModel() {

    private val _isLoggedIn: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoggedIn.emit(localRepository.isUserLoggedIn() ?: false)
        }
    }
}