package com.example.quizhuntercompose.feature_pickTest.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "topic_table",  primaryKeys = ["topic_id", "test_id"])
data class Topic(
//    @PrimaryKey
    @ColumnInfo(name = "topic_id") val topicId: Int,
    @ColumnInfo(name = "topic") val topic: String,
    @ColumnInfo(name = "test_id") val testId: Int,
) {
}