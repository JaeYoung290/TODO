package com.example.todolist.module

import android.content.Context
import androidx.room.Room
import com.example.data.naver.calendar.repository.NaverApiRepositoryImpl
import com.example.data.notice.repository.NoticeRepositoryImpl
import com.example.data.notice.repository.WebPageRepositoryImpl
import com.example.data.notice.source.notice.NoticeDao
import com.example.data.notice.source.notice.NoticeDatabase
import com.example.data.naver.calendar.repository.remote.datasource.calendar.RemoteCalendarDataSourceImpl
import com.example.data.naver.calendar.repository.remote.datasource.user.RemoteUserDataSourceImpl
import com.example.data.todo.TodoDao
import com.example.data.todo.TodoDatabase
import com.example.data.todo.TodoRepositoryImpl
import com.example.domain.naver.calendar.repository.NaverApiRepository
import com.example.domain.notice.repository.NoticeRepository
import com.example.domain.notice.repository.WebPageRepository
import com.example.domain.notice.useCase.database.DatabaseUseCases
import com.example.domain.notice.useCase.database.DeleteNotice
import com.example.domain.notice.useCase.database.FavoriteNotice
import com.example.domain.notice.useCase.database.GetAllNotices
import com.example.domain.notice.useCase.database.GetDeletedItem
import com.example.domain.notice.useCase.database.GetFavoriteItem
import com.example.domain.notice.useCase.database.GetNoticeByCategory
import com.example.domain.notice.useCase.webPage.OpenUrlByBrowser
import com.example.domain.notice.useCase.webPage.ParseWebPages
import com.example.domain.notice.useCase.webPage.WebPageUseCases
import com.example.domain.todo.repository.TodoRepository
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
        return TodoRepositoryImpl(todoDao)
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

    @Singleton
    @Provides
    fun provideWebPageRepository(noticeDao: NoticeDao): WebPageRepository {
        return WebPageRepositoryImpl(noticeDao)
    }

    @Singleton
    @Provides
    fun provideWebPageUseCases(repository: WebPageRepository): WebPageUseCases {
        return WebPageUseCases(
            parseWebPages = ParseWebPages(repository),
            openUrlByBrowser = OpenUrlByBrowser()
        )
    }

    @Singleton
    @Provides
    fun provideDatabaseUseCases(repository: NoticeRepository): DatabaseUseCases {
        return DatabaseUseCases(
            getNoticeByCategory = GetNoticeByCategory(repository),
            getAllNotices = GetAllNotices(repository),
            deleteNotice = DeleteNotice(repository),
            favoriteNotice = FavoriteNotice(repository),
            getDeletedItem = GetDeletedItem(repository),
            getFavoriteItem = GetFavoriteItem(repository)
        )
    }
}