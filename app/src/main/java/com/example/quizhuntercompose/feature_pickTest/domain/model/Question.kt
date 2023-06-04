package com.example.quizhuntercompose.feature_pickTest.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity(tableName = "question_table", primaryKeys = ["question_id", "test_id"])
data class Question(

    @ColumnInfo(name = "question_id") val questionID: Int,
    @ColumnInfo(name = "test_id") val testID: Int,
//    @PrimaryKey(autoGenerate = true)
//    (q),
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "answer_1") val answer1: String,
    @ColumnInfo(name = "answer_2") val answer2: String,
    @ColumnInfo(name = "answer_3") val answer3: String?,
    @ColumnInfo(name = "answer_4") val answer4: String?,
    @ColumnInfo(name = "correct_answer") val correctAnswer: Int,
    @ColumnInfo(name = "topic_id") val topic: Int, //I had naming as "topic" in my table. Should export schema from app.
    @ColumnInfo(name = "explanation") val explanation: String?,
    @ColumnInfo(name = "imageUrl") val imageUrl: String?,

    @ColumnInfo(name = "correct_answers") val correctAnswers: Int,
    @ColumnInfo(name = "wrong_answers") val wrongAnswers: Int,
    @ColumnInfo(name = "non_answers") val nonAnswers: Int,

    @ColumnInfo(name = "average_time_sec") val averageAnswerTime: Int,
    @ColumnInfo(name = "last_time_sec") val lastAnswerTime: Int //Milli sec

)

data class FirebaseQuestion(
    val questionID: Int,

    val question: String,
    val answer1: String,
    val answer2: String,
    val answer3: String?,
    val answer4: String?,
    val correctAnswer: Int,
    val topic: Int, //I had naming as "topic" in my table. Should export schema from app.
    val explanation: String?,
    val imageUrl: String?,
)

data class FirebaseUserQuestionData(
    val questionID: Int,
    val testId: Int,

    val correctAnswers: Int,
    val wrongAnswers: Int,
    val nonAnswers: Int,
    val averageAnswerTime: Int,
    val lastAnswerTime: Int //Milli sec
)

