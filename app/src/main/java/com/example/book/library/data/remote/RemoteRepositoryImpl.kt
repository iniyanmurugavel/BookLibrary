package com.example.book.library.data.remote

import com.example.book.library.data.dto.DtoMapper.toBookListItemDomain
import com.example.book.library.data.dto.DtoMapper.toCountryDomain
import com.example.book.library.data.dto.DtoMapper.toCountryListDomain
import com.example.book.library.domain.IRemoteRepository
import com.example.book.library.domain.model.BookListDataItem
import com.example.book.library.domain.model.Country
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val service: ApiService) : IRemoteRepository {


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
      val response = service.getBookList()
      return if(response.isSuccessful){
          response.body()?.toBookListItemDomain().orEmpty()
      }else{
          emptyList()
      }
    }
}
