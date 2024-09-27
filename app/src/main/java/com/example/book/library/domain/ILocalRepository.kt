package com.example.book.library.domain

import com.example.book.library.data.UserEntity

interface ILocalRepository {

    suspend fun isUserLoggedIn() : Boolean?

    suspend fun setUserLoggedIn(isLoggedIn: Boolean)

    suspend fun setUserData(userEntity: UserEntity)

    suspend fun getUserData() : UserEntity

}