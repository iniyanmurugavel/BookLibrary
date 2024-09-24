package com.example.book.library.domain

interface ILocalRepository {

    suspend fun isUserLoggedIn() : Boolean?

    suspend fun setUserLoggedIn(isLoggedIn: Boolean)

}