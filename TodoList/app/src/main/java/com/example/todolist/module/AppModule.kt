package com.example.todolist.module

import android.content.Context
import com.example.data.TodoDao
import com.example.data.TodoDatabase
import com.example.data.TodoRepository
import com.example.data.naver.calendar.repository.NaverApiRepositoryImpl
import com.example.data.naver.calendar.repository.remote.datasource.calendar.RemoteCalendarDataSourceImpl
import com.example.data.naver.calendar.repository.remote.datasource.user.RemoteUserDataSourceImpl
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
    fun provideNaverApiRepository(
        remoteUserDataSourceImpl: RemoteUserDataSourceImpl,
        remoteCalendarDataSourceImpl: RemoteCalendarDataSourceImpl
    ) : NaverApiRepository {
        return NaverApiRepositoryImpl(remoteUserDataSourceImpl, remoteCalendarDataSourceImpl)
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

    @Singleton
    @Provides
    fun provideRemoteUserDataSource() : RemoteUserDataSourceImpl {
        return RemoteUserDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideRemoteCalendarDataSource() : RemoteCalendarDataSourceImpl {
        return RemoteCalendarDataSourceImpl()
    }
}