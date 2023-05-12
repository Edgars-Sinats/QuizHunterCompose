package com.example.quizhuntercompose.domain.model

import androidx.room.PrimaryKey
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.google.firebase.firestore.ServerTimestamp

data class FirebaseWholeTest (
    @PrimaryKey
    val testID: Int, //TODO rename QuestionDao table name with new one, and add new table?
    val testName: String,
    val testDescription: String = "",
//    @ServerTimestamp
    val dateCreated: String,
    @ServerTimestamp
    val dateModified: String = "",
    /** Note. [Favorite] - only in Room */
    val needKey: Boolean,
    val additionalInfo: String = "",
    val language: String,
    val topics: List<Topic>,
    val questions: List<Question>,
        )