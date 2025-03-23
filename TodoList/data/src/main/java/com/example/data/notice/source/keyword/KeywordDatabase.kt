package com.example.data.notice.source.keyword

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [KeywordEntity::class], version = 1)
abstract class KeywordDatabase: RoomDatabase() {
    abstract fun keywordDao(): KeywordDao

    companion object {
        @Volatile
        private var database: KeywordDatabase? = null

        fun getDatabase(context: Context): KeywordDatabase {
            return database ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KeywordDatabase::class.java,
                    "keyword_Database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                database = instance
                instance
            }
        }
    }
}