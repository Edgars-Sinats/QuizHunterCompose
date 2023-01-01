package com.example.quizhuntercompose.feature_pickTest.db.repository

import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuestionDao
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl@Inject constructor(
    private val dao: QuestionDao
//    private val database: QuizDatabase,
) : QuestionRepository {

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

    override suspend fun getXQuestionFromTopic(topic: String, count: Int): List<Question> {
        return dao.getXFromTopic(topic, count)
    }

    override suspend fun updateQuestion(question: Question) {
        dao.updateQuestion(question = question)
    }

    override suspend fun updateTopics(topics: List<Topic>) {
        dao.updateTopics(topics)
    }


    override fun getAllTopics(): List<Topic> {
        return dao.getAllTopic()
    }

}