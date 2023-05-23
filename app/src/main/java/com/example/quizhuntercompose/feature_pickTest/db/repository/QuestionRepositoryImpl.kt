package com.example.quizhuntercompose.feature_pickTest.db.repository

import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuestionDao
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val dao: QuestionDao
//    private val database: QuizDatabase,
) : QuestionRepository {

    override suspend fun getQuestionCountChecker(ids: List<Int>, testId:Int, nonAns: Boolean, wrongAns: Boolean): Int {
        return dao.getQuestionCountChecker(ids, testId, nonAns, wrongAns)
    }

    override suspend fun getMyQuestions(ids: List<Int>?, nonAns: Boolean, wrongAns: Boolean, count: Int, testId: Int): List<Question> {
        return dao.getMyQuestions(count,  nonAns, wrongAns, ids, testId = testId)
    }

    override suspend fun getQuestionCount(topic_id: Int, testId: Int): Int {
        return dao.getQuestionCount(topic_id, testId = testId)
    }

    override suspend fun getQuestionCountFrom(topic_ids: List<Int>, noAns: Int, testId: Int) : Int{
        return dao.getQuestionCountFrom(topic_ids, noAns, testId = testId)
    }

    override fun getAllQuestions(testId: Int): List<Question> {
        return dao.getAllQuestions(testId = testId)
    }

    override suspend fun getStartTest(testId: Int): List<Question> {
        return dao.getStartTest(testId = testId)
    }

    override suspend fun getXQuestions(count: Int, testId: Int): List<Question> {
        return dao.getQuestionsX(count, testId = testId)
    }

    override suspend fun getQuestionById(id: Int, testId: Int): Question? {
        return dao.getQuestionX(id,testId)
    }

    override suspend fun getXQuestionFromTopic(topic: Int, count: Int, testId: Int): List<Question> {
        return dao.getXFromTopic(topic, count, testId)
    }

    override suspend fun updateQuestion(question: Question) {
        dao.updateQuestion(question = question)
    }

    override suspend fun updateTopics(topics: List<Topic>) {
        dao.updateTopics(topics)
    }


    override suspend fun getAllTopics(testId: Int): List<Topic> {
        return dao.getAllTopic(testId = testId)
    }

}