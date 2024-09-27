package com.example.book.library.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.book.library.data.UserEntity
import com.example.book.library.domain.ILocalRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


const val DATASTORE_NAME = "BOOK_LIBRARY"

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class LocalRepositoryImpl(private val context: Context) : ILocalRepository {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("IS_LOGGED_IN")
        val EMAIL = stringPreferencesKey("EMAIL")
        val PASSWORD = stringPreferencesKey("PASSWORD")
        val COUNTRY = stringPreferencesKey("COUNTRY")
    }

    override suspend fun isUserLoggedIn(): Boolean? = context.datastore.data.map { preference ->
        preference[IS_LOGGED_IN]
    }.first()

    override suspend fun setUserLoggedIn(isLoggedIn: Boolean) {
        context.datastore.edit { preference ->
            preference[IS_LOGGED_IN] = isLoggedIn
        }
    }

    override suspend fun setUserData(userEntity: UserEntity) {
        context.datastore.edit { preference ->
            preference[EMAIL] = userEntity.mail
            preference[PASSWORD] = userEntity.password
            preference[COUNTRY] = userEntity.country
        }
    }

    override suspend fun getUserData(): UserEntity = context.datastore.data.map { preference ->
        UserEntity(
            mail = preference[EMAIL].orEmpty(),
            password = preference[PASSWORD].orEmpty(),
            country = preference[COUNTRY].orEmpty()
        )
    }.first()
}