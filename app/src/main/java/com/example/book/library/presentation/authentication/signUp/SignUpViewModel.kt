package com.example.book.library.presentation.authentication.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book.library.data.UserEntity
import com.example.book.library.domain.ILocalRepository
import com.example.book.library.domain.model.Country
import com.example.book.library.domain.usecase.FetchCountriesUsecase
import com.example.book.library.domain.usecase.FetchDefaultCountryUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val localRepository: ILocalRepository,
    private val fetchCountryUsecase: FetchCountriesUsecase,
    private val fetchDefaultCountryUsecase: FetchDefaultCountryUsecase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    init {
        fetchCountries()
    }

    fun setUserLoggedIn(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.setUserLoggedIn(true)
            localRepository.setUserData(userEntity)
        }
    }

    private fun fetchCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            val countries = async { fetchCountryUsecase.invoke() }
            val defaultCountry = async { fetchDefaultCountryUsecase() }
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