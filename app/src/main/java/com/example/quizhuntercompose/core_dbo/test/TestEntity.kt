package com.example.quizhuntercompose.core_dbo.test

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_table")
data class TestEntity(
    @PrimaryKey
    @ColumnInfo(name = "test_id") val testId: Int,
    @ColumnInfo(name = "test_name") val testName: String,
    @ColumnInfo(name = "test_image_url") val testImageUrl: String?,
    @ColumnInfo(name = "test_description") val testDescription: String?,
//    @ServerTimestamp
    @ColumnInfo(name = "test_created") val testCreated: String, //(date_created)
//    @ServerTimestamp
    @ColumnInfo(name = "test_modified") val testModified: String?, //(date_modified)

    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,  //Not in Firebase -> In Fire. -> Under user.
    @ColumnInfo(name = "need_key") val needKey: Boolean,
    @ColumnInfo(name = "additional_info") val additionalInfo: String?,
//    @ColumnInfo(name = "testAdmin") val testAdmin: String,
//    @ColumnInfo(name = "testAdmin") val testTeachers: String,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "isLocal") val isLocal: Boolean,
    @ColumnInfo(name = "testRank") val testRank: Int

)
