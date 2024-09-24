package com.example.book.library.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.book.library.domain.ILocalRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


const val DATASTORE_NAME = "BOOK_LIBRARY"

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class LocalRepositoryImpl(private val context: Context) : ILocalRepository {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("IS_LOGGED_IN")
    }

    override suspend fun isUserLoggedIn(): Boolean? = context.datastore.data.map { preference ->
        preference[IS_LOGGED_IN]
    }.first()

    override suspend fun setUserLoggedIn(isLoggedIn: Boolean) {
        context.datastore.edit { phonebooks ->
            phonebooks[IS_LOGGED_IN] = isLoggedIn
        }
    }
}