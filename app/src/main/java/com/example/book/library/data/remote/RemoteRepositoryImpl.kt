package com.example.book.library.data.remote

import com.example.book.library.data.dto.DtoMapper.toCountryDomain
import com.example.book.library.data.dto.DtoMapper.toCountryListDomain
import com.example.book.library.domain.IRemoteRepository
import com.example.book.library.domain.model.BookListDataItem
import com.example.book.library.domain.model.Country
import com.example.book.library.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteRepositoryImpl : IRemoteRepository {

    private val service = ApiClient.retrofit

    override suspend fun fetchCountries(): List<Country> {
        val response = service.getCountries()
        return if (response.isSuccessful) {
            response.body()?.toCountryListDomain().orEmpty()
        } else {
            emptyList()
        }
    }

    override suspend fun fetchDefaultCountry(): Country? {
        val response = service.getDefaultCountry()
        return if (response.isSuccessful) {
            response.body()?.toCountryDomain()
        } else {
            null
        }
    }

    override suspend fun getBookList(): List<BookListDataItem> {
        return withContext(Dispatchers.IO){
            service.getBookList().body().orEmpty()
        }
    }
}