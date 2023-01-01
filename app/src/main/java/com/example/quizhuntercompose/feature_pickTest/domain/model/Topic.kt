package com.example.quizhuntercompose.feature_pickTest.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "topic_table")
data class Topic(
    @PrimaryKey
    @ColumnInfo(name = "topic_id") val topicId: Int,
    @ColumnInfo(name = "topic") val topic: String

) {
}