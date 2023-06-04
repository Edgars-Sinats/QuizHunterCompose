package com.example.quizhuntercompose.feature_pickTest.domain.repository

import android.media.Image
import coil.request.ImageRequest
import com.example.quizhuntercompose.cor.util.Resource
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

//    fun getImageRemote(image: String): Flow<Resource<Image>>
//    // answerTime: Boolean
    suspend fun getQuestionCountChecker(ids: List<Int>, testId: Int, nonAns: Boolean, wrongAns: Boolean): Int

    suspend fun getQuestionCountCheckerByTime(ids: List<Int>, testId: Int, nonAns: Boolean, wrongAns: Boolean, time: Boolean): Int


    suspend fun getMyQuestions(ids: List<Int>?, nonAns: Boolean, wrongAns: Boolean, count: Int, testId: Int, time: Boolean): List<Question>

    suspend fun getQuestionCount(topic_id: Int, testId: Int): Int

    suspend fun getQuestionCountFrom(topic_ids: List<Int>, noAns: Int, testId: Int) : Int

    fun getAllQuestions(testId: Int): List<Question>

    suspend fun getStartTest(testId: Int): List<Question>

    suspend fun getXQuestions(count: Int, testId: Int): List<Question>

    suspend fun getQuestionById(id: Int, testId: Int): Question?

    suspend fun getXQuestionFromTopic(topic: Int, count: Int, testId: Int): List<Question>

    suspend fun updateQuestion(question: Question)

    suspend fun updateTopics(topics: List<Topic>)

    suspend fun getAllTopics(testId: Int): List<Topic>

}