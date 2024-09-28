package com.example.book.library.data.remote

import com.example.book.library.data.dto.CountryDto
import com.example.book.library.domain.model.BookListDataItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("b/IU1K")
    suspend fun getCountries(): Response<List<CountryDto>>

    @GET("http://ip-api.com/json")
    suspend fun getDefaultCountry(): Response<CountryDto>

    @GET("b/CNGI")
    suspend fun getBookList() : Response<List<BookListDataItem>>
}