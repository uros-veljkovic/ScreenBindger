package com.example.screenbindger.db.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.screenbindger.db.local.dao.UserDao
import com.example.screenbindger.db.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [UserEntity::class], version = 1)
abstract class ScreenBindgerDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    fun isLoggedIn(): Flow<Boolean?> {
        return userDao().isLoggedIn()
    }

}
