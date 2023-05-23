package com.example.quizhuntercompose.cor.util

import androidx.room.TypeConverter
import com.google.firebase.Timestamp

class TimestampConverter {
    @TypeConverter
    fun toDatabaseValue(timestamp: Timestamp?): Long? {
        return timestamp?.seconds
    }

    @TypeConverter
    fun toEntityValue(seconds: Long?): Timestamp? {
        return seconds?.let { Timestamp(it, 0) }
    }
}