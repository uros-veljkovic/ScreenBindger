package com.example.screenbindger.db.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey val _id: Int,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "date_of_birth") val dateOfBirth: Long?,
    @ColumnInfo(name = "is_logged_in") val isLoggedIn: Boolean
) {
}