package com.example.findmypackage.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.findmypackage.data.db.postList.PostListEntity
import com.example.findmypackage.data.db.postList.PostListDao

class Database (private val applicationContext: Context) {
    companion object {
        private const val DB_NAME = "my_db"
    }

    fun invoke(): PostDatabase {
        return Room.databaseBuilder(applicationContext,
            PostDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}

@Database(entities = [PostListEntity::class], version = 1)
abstract class PostDatabase: RoomDatabase() {
    abstract fun postListDao(): PostListDao
}