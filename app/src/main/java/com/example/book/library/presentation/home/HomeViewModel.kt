package com.example.book.library.presentation.home

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.book.library.data.UserEntity
import com.example.book.library.domain.ILocalRepository
import com.example.book.library.domain.IRemoteRepository
import com.example.book.library.domain.model.BookListDataItem
import com.example.book.library.domain.usecase.FetchBooksListUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localRepository: ILocalRepository,
    private val fetchBooksListUsecase: FetchBooksListUsecase
) : ViewModel() {

    private val _uiEffect: Channel<HomeEffect> = Channel()
    val uiEffect: Flow<HomeEffect> = _uiEffect.receiveAsFlow()

    private val _uiState = MutableStateFlow(BookState())

    val uiState: StateFlow<BookState>
        get() = _uiState

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.setUserLoggedIn(false)
            _uiEffect.send(HomeEffect.NavigateToLogin)
        }
    }

    fun getBookList() {
        _uiState.update { currentState->
            currentState.copy(
                booksList = null,
                isLoading = true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val data = fetchBooksListUsecase.invoke()
            _uiState.update { currentState ->
                currentState.copy(
                    booksList = data,
                    isLoading = false
                )
            }
        }
    }


    data class BookState(
        val booksList: Map<Int, List<BookListDataItem>>? = null,
        val isLoading : Boolean = false
    )

    sealed class HomeEffect {
        data object NavigateToLogin : HomeEffect()
    }
}