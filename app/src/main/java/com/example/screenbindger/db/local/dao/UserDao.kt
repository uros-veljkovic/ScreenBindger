package com.example.screenbindger.db.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.screenbindger.db.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT is_logged_in FROM userentity WHERE is_logged_in == 1")
    suspend fun isLoggedIn(): Boolean?
}