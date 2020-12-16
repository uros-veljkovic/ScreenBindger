package com.example.screenbindger.db.local.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.screenbindger.db.local.dao.UserDao
import com.example.screenbindger.db.local.entity.user.UserEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [UserEntity::class], version = 4, exportSchema = false)
abstract class ScreenBindgerLocalDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    fun isLoggedIn(): Flow<Boolean?> {
        return userDao().isLoggedIn()
    }

    suspend fun register(userEntity: UserEntity){
        userDao().register(userEntity)
    }

    suspend fun update(userEntity: UserEntity){
        userDao().update(userEntity)
    }

    suspend fun find(userEntity: UserEntity): UserEntity?{
        val user = userDao().find(userEntity.email, userEntity.password)
        return user
    }

    suspend fun login(userEntity: UserEntity){
        userDao().login(userEntity._id)
    }

    suspend fun findLoggedInUser() : UserEntity{
        return userDao().findLoggedInUser()
    }

}
