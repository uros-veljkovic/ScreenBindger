package com.example.screenbindger.db.local.dao

import androidx.room.*
import com.example.screenbindger.db.local.entity.user.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT is_logged_in FROM userentity WHERE is_logged_in == 1")
    fun isLoggedIn(): Flow<Boolean?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun register(userEntity: UserEntity)

    @Query("SELECT * FROM userentity WHERE email = :email")
    suspend fun find(email: String): UserEntity?

    @Query("SELECT * FROM userentity WHERE email = :email AND password = :password")
    suspend fun authorize(email: String, password: String): UserEntity?


    @Update
    suspend fun update(userEntity: UserEntity)

    @Query("UPDATE userentity SET is_logged_in = 1 WHERE _id = :id")
    suspend fun login(id: Int?)

    @Query("UPDATE userentity SET is_logged_in = 0 WHERE _id = :id")
    suspend fun logout(id: Int?)

    @Query("SELECT * FROM userentity WHERE is_logged_in = 1")
    suspend fun findLoggedInUser(): UserEntity
}