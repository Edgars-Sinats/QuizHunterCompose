package com.example.quizhuntercompose.feature_pickTest.db.repository

import android.util.Log
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.question1
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class TestQuestionRepository : QuestionRepository {

    private val questionFlow: MutableSharedFlow<List<Question>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val questionFlow1: MutableStateFlow<List<Question>> =
        MutableStateFlow(listOf())

    private val topicFlow: MutableSharedFlow<List<Topic>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val topicFlow1: MutableStateFlow<List<Topic>> =
        MutableStateFlow(listOf())



    override suspend fun getQuestionCountChecker(ids: List<Int>, nonAns: Boolean, wrongAns: Boolean): Int {
        //TODO
        return questionFlow1.value.map { question -> question.wrongAnswers > 0 == wrongAns && question.nonAnswers > 0 == nonAns }.count()
    }

    //TODO add limit functionality by count
    override suspend fun getMyQuestions(
        ids: List<Int>?,
        nonAns: Boolean,
        wrongAns: Boolean,
        count: Int
    ): List<Question> {
        if (ids != null) {
            return questionFlow1.value.filter { question ->
                ids.contains(question.questionID) &&
                if(nonAns) {
                    question.wrongAnswers+question.correctAnswers==0 }
                else if (wrongAns){
                    question.wrongAnswers >= question.correctAnswers }
                else {true}
            }
        } else {
            return questionFlow1.value.filter { question ->
                if(nonAns) {
                    question.wrongAnswers+question.correctAnswers==0 }
                else if (wrongAns){
                    question.wrongAnswers >= question.correctAnswers }
                else {true}
            }
        }
    }

    override suspend fun getQuestionCount(topic_id: Int): Int {

        return questionFlow1.value.filter { question -> question.topic == topic_id }.size
//        return questionFlow1.map { question -> question.find { it.topic == topic_id } }.count()
    }

    override suspend fun getQuestionCountFrom(topic_ids: List<Int>, noAns: Int): Int {
        val x1 = questionFlow1.value.toMutableList()
        val x2  = x1.iterator()

        while( x2.hasNext()) {
            val current = x2.next()
            if (
                current.nonAnswers <= noAns &&
                topic_ids.contains(current.topic)
            ){
                continue
            } else {
                x1.remove(current)
                x2.remove()
            }
        }
        return x1.size
    }

    override fun getAllQuestions(): List<Question> {
        return questionFlow1.value
    }

    override suspend fun getStartTest(): List<Question> {
        return questionFlow1.value.subList(0,2)//    ORDER BY random() LIMIT 3
    }

    override suspend fun getXQuestions(count: Int): List<Question> {
        return questionFlow1.value.subList(0,count)
    }

    override suspend fun getQuestionById(id: Int): Question? {
        return questionFlow1.value.find { question -> question.questionID == id }
    }

    override suspend fun getXQuestionFromTopic(topic: Int, count: Int): List<Question> {
//        return questionFlow1.value.subList()
        TODO()
    }

    /**
     * A test-only API to allow controlling the list of topics from tests.
     */
    override suspend fun updateQuestion(question: Question) {
        questionFlow1.value = questionFlow1.value + question
//        questionFlow1.value = listOf( questionFlow1.value[questionFlow1.value.count()+1] , question)
//        questionFlow.tryEmit(listOf(quest   ion))
    }

    /**
     * A test-only API to allow controlling the list of topics from tests.
     */
    override suspend fun updateTopics(topics: List<Topic>) {
        topicFlow1.tryEmit(topics)
    }

    override suspend fun getAllTopics(): List<Topic> {
        return topicFlow1.value
//        return topicFlow.map { value: List<Topic> -> value.forEachIndexed() }
    }
}