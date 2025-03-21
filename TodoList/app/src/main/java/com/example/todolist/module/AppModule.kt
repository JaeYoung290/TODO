package com.example.todolist.module

import android.content.Context
import androidx.room.Room
import com.example.data.TodoDao
import com.example.data.TodoDatabase
import com.example.data.TodoRepository
import com.example.data.naver.calendar.repository.NaverApiRepositoryImpl
import com.example.data.notice.repository.NoticeRepositoryImpl
import com.example.data.notice.source.notice.NoticeDao
import com.example.data.notice.source.notice.NoticeDatabase
import com.example.data.naver.calendar.repository.remote.datasource.calendar.RemoteCalendarDataSourceImpl
import com.example.data.naver.calendar.repository.remote.datasource.user.RemoteUserDataSourceImpl
import com.example.domain.naver.calendar.repository.NaverApiRepository
import com.example.domain.notice.repository.NoticeRepository
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

    @Singleton
    @Provides
    fun provideNoticeDao(@ApplicationContext context: Context): NoticeDao {
        val db = Room.databaseBuilder(context, NoticeDatabase::class.java, "notice_db").build()
        return db.noticeDao()
    }

    @Singleton
    @Provides
    fun provideNoticeRepository(noticeDao: NoticeDao): NoticeRepository {
        return NoticeRepositoryImpl(noticeDao)
    }
}