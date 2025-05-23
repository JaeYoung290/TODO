package com.example.data.notice.source.notice

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

@Database(entities = [NoticeEntity::class, NoticeFts::class], version = 2)
abstract class NoticeDatabase : RoomDatabase() {
    abstract fun noticeDao(): NoticeDao

    companion object {
        @Volatile
        private var database: NoticeDatabase? = null

        fun getDatabase(context: Context): NoticeDatabase {
            return database ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoticeDatabase::class.java,
                    "notice_Database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                database = instance
                instance
            }
        }
    }
}