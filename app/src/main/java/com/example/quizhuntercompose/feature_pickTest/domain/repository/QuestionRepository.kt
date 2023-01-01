package com.example.quizhuntercompose.feature_pickTest.domain.repository

import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic

interface QuestionRepository {

    fun getAllQuestions(): List<Question>

    suspend fun getStartTest(): List<Question>

    suspend fun getXQuestions(count: Int): List<Question>

    suspend fun getQuestionById(id: Int): Question?

    suspend fun getXQuestionFromTopic(topic: String, count: Int): List<Question>

    suspend fun updateQuestion(question: Question)

    suspend fun updateTopics(topics: List<Topic>)

    fun getAllTopics(): List<Topic>

}