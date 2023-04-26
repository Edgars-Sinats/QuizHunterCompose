package com.example.quizhuntercompose.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_keys")
data class UserKeys(
    @PrimaryKey
    @ColumnInfo(name = "test_id") val testId: Int,
    @ColumnInfo(name = "test_key") val testKey: String
)
