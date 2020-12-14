package com.example.screenbindger.db.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.screenbindger.db.local.entity.user.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT is_logged_in FROM userentity WHERE is_logged_in == 1")
    fun isLoggedIn(): Flow<Boolean?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun register(userEntity: UserEntity)

    @Query("SELECT * FROM userentity WHERE email = :email AND password = :password")
    suspend fun find(email: String, password: String): UserEntity?

    @Query("UPDATE userentity SET is_logged_in = 1 WHERE _id = :id")
    suspend fun login(id: Int)
}