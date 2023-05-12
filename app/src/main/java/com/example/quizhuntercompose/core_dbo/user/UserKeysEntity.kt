package com.example.quizhuntercompose.core_dbo.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@Entity(tableName = "user_keys")
data class UserKeysEntity(
    @PrimaryKey
    @ColumnInfo(name = "test_id") val testId: Int,
    @ColumnInfo(name = "test_key") val testKey: String,
    @ColumnInfo(name = "test_key_valid_date") val testKeyValidDate: String
)
