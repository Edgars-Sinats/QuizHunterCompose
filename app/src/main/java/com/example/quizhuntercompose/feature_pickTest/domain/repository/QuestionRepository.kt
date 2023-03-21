package com.example.quizhuntercompose.feature_pickTest.domain.repository

import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic

interface QuestionRepository {

    suspend fun getQuestionCountChecker(ids: List<Int>, nonAns: Boolean, wrongAns: Boolean): Int

    suspend fun getMyQuestions(ids: List<Int>?, nonAns: Boolean, wrongAns: Boolean, count: Int): List<Question>

    suspend fun getQuestionCount(topic_id: Int): Int

    suspend fun getQuestionCountFrom(topic_ids: List<Int>, noAns: Int) : Int

    fun getAllQuestions(): List<Question>

    suspend fun getStartTest(): List<Question>

    suspend fun getXQuestions(count: Int): List<Question>

    suspend fun getQuestionById(id: Int): Question?

    suspend fun getXQuestionFromTopic(topic: Int, count: Int): List<Question>

    suspend fun updateQuestion(question: Question)

    suspend fun updateTopics(topics: List<Topic>)

    suspend fun getAllTopics(): List<Topic>

}