package com.example.todolist.module

import android.content.Context
import com.example.data.TodoDao
import com.example.data.TodoDatabase
import com.example.data.TodoRepository
import com.example.data.naver.calendar.repository.NaverApiRepositoryImpl
import com.example.domain.naver.calendar.repository.NaverApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideNaverApiRepository() : NaverApiRepository {
        return NaverApiRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideTodoDao(@ApplicationContext context: Context) : TodoDao {
        return TodoDatabase.getDatabase(context).todoDao()
    }

    @Singleton
    @Provides
    fun provideTodoRepository(
        todoDao: TodoDao
    ) : TodoRepository {
        return TodoRepository(todoDao)
    }
}