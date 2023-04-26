package com.example.quizhuntercompose.feature_pickTest.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_table")
data class Question(
    @ColumnInfo(name = "question") val question: String,

    @ColumnInfo(name = "answer_1") val answer1: String,
    @ColumnInfo(name = "answer_2") val answer2: String,
    @ColumnInfo(name = "answer_3") val answer3: String?,
    @ColumnInfo(name = "answer_4") val answer4: String?,
    @ColumnInfo(name = "correct_answer") val correctAnswer: Int,
    @PrimaryKey
    @ColumnInfo(name = "question_id") val questionID: Int,
    @ColumnInfo(name = "topic_id") val topic: Int, //I had naming as "topic" in my table. Should export schema from app.
    @ColumnInfo(name = "explanation") val explanation: String?,

    @ColumnInfo(name = "correct_answers") val correctAnswers: Int,
    @ColumnInfo(name = "wrong_answers") val wrongAnswers: Int,
    @ColumnInfo(name = "non_answers") val nonAnswers: Int,

    @ColumnInfo(name = "average_time_sec") val averageAnswerTime: Int,
    @ColumnInfo(name = "last_time_sec") val lastAnswerTime: Int //Milli sec

) {
}