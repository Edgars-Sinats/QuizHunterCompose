package com.example.quizhuntercompose.feature_pickTest.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.test.services.events.TimeStamp

/**
 * [needKey] is boolean value for premium quiz,
 * if true it can be fully accessed only if user is Premium
 * or user has his own key for current [testID].
 *
 * [language] is quiz native language
 * [additionalInfo] is optional detailed info of test.
 * F.e. - Latvian official hunter licence test questions.
 * [dateModified] last time test was modified.
 */
@Entity(tableName = "test_table")
data class Test(
    @PrimaryKey
      @ColumnInfo(name = "test_id") val testID: Int, //TODO rename QuestionDao table name with new one, and add new table?
    @ColumnInfo(name = "test_name") val testName: String,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "additional_info") val additionalInfo: String,
    @ColumnInfo(name = "date_created") val dateCreated: TimeStamp,
    @ColumnInfo(name = "date_modified") val dateModified: TimeStamp,
    @ColumnInfo(name = "need_key") val needKey: Boolean,
    @ColumnInfo(name = "questions") val questions: List<Question>,
    @ColumnInfo(name = "topics") val topics: List<Topic>,
    )
