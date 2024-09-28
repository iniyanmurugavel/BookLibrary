package com.example.book.library.domain

import com.example.book.library.domain.model.BookListDataItem
import com.example.book.library.domain.model.Country

interface IRemoteRepository {

    suspend fun fetchCountries() : List<Country>

    suspend fun fetchDefaultCountry() : Country?

    suspend fun getBookList() : List<BookListDataItem>
}