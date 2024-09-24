package com.example.book.library.di

import android.content.Context
import com.example.book.library.data.local.LocalRepositoryImpl
import com.example.book.library.data.remote.RemoteRepositoryImpl
import com.example.book.library.domain.ILocalRepository
import com.example.book.library.domain.IRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesILocalRepository(
        @ApplicationContext context: Context
    ): ILocalRepository {
        return LocalRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun providesIRemoteRepository(
    ): IRemoteRepository {
        return RemoteRepositoryImpl()
    }
}