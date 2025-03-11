package com.example.todolist.module

import com.example.data.naver.calendar.repository.NaverApiRepository
import com.example.data.naver.calendar.repository.NaverApiRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideNaverApiRepository() : NaverApiRepository{
        return NaverApiRepositoryImpl()
    }
}