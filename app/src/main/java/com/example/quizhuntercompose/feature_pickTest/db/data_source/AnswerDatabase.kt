package com.example.quizhuntercompose.feature_pickTest.db.data_source

import androidx.room.RoomDatabase

abstract class AnswerDatabase: RoomDatabase() {
    abstract val questionDao: QuestionDao


}