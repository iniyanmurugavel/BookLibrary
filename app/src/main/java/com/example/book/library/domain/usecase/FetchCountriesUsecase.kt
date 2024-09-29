package com.example.book.library.domain.usecase

import com.example.book.library.domain.IRemoteRepository
import com.example.book.library.domain.model.Country
import javax.inject.Inject

class FetchCountriesUsecase @Inject constructor(private val iRemoteRepository: IRemoteRepository) {


    suspend operator fun invoke():  List<Country> {
        return iRemoteRepository.fetchCountries()
    }
    


}