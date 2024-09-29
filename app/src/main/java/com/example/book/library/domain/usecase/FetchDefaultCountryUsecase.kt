package com.example.book.library.domain.usecase

import com.example.book.library.domain.IRemoteRepository
import com.example.book.library.domain.model.BookListDataItem
import com.example.book.library.domain.model.Country
import java.util.Calendar
import javax.inject.Inject

class FetchDefaultCountryUsecase @Inject constructor(private val iRemoteRepository: IRemoteRepository) {


    suspend operator fun invoke(): Country? {
        return iRemoteRepository.fetchDefaultCountry()
    }



}