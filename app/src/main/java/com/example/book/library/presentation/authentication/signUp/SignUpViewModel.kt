package com.example.book.library.presentation.authentication.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book.library.domain.ILocalRepository
import com.example.book.library.domain.IRemoteRepository
import com.example.book.library.domain.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val remoteRepository: IRemoteRepository,
    private val localRepository: ILocalRepository,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    init {
        fetchCountries()
    }

    fun setUserLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.setUserLoggedIn(true)
        }
    }

    private fun fetchCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            val countries = async { remoteRepository.fetchCountries() }
            val defaultCountry = async { remoteRepository.fetchDefaultCountry() }
            val countriesResult = countries.await()
            val defaultCountryResult = defaultCountry.await()
            _uiState.value = _uiState.value.copy(
                countries = countriesResult.sortedBy { it.country },
                selectedCountry = countriesResult.find { it.country == defaultCountryResult?.country }
            )
        }
    }
}

data class SignUpUiState(
    val countries: List<Country>? = null,
    val selectedCountry: Country? = null,
)