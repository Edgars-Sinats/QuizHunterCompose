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

    override suspend fun getQuestionCountChecker(ids: List<Int>, nonAns: Boolean, wrongAns: Boolean): Int {
        return dao.getQuestionCountChecker(ids, nonAns, wrongAns)
    }

    override suspend fun getMyQuestions(ids: List<Int>?, nonAns: Boolean, wrongAns: Boolean, count: Int): List<Question> {
        return dao.getMyQuestions(count,  nonAns, wrongAns, ids)
    }

    override suspend fun getQuestionCount(topic_id: Int): Int {
        return dao.getQuestionCount(topic_id)
    }

    override suspend fun getQuestionCountFrom(topic_ids: List<Int>, noAns: Int) : Int{
        return dao.getQuestionCountFrom(topic_ids, noAns)
    }

    override fun getAllQuestions(): List<Question> {
        return dao.getAllQuestions()
    }

    override suspend fun getStartTest(): List<Question> {
        return dao.getStartTest()
    }

    override suspend fun getXQuestions(count: Int): List<Question> {
        return dao.getQuestionsX(count)
    }

    override suspend fun getQuestionById(id: Int): Question? {
        return dao.getQuestionX(id)
    }

    override suspend fun getXQuestionFromTopic(topic: Int, count: Int): List<Question> {
        return dao.getXFromTopic(topic, count)
    }

    override suspend fun updateQuestion(question: Question) {
        dao.updateQuestion(question = question)
    }

    override suspend fun updateTopics(topics: List<Topic>) {
        dao.updateTopics(topics)
    }


    override suspend fun getAllTopics(): List<Topic> {
        return dao.getAllTopic()
    }

}