package com.example.quizhuntercompose.feature_pickTest.db.repository

import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository

class FakeQuestionRepository : QuestionRepository {

    private val questions = mutableListOf <Question>()
    private val topics = mutableListOf<Topic>()

    override suspend fun getQuestionCountChecker(
        ids: List<Int>,
        nonAns: Boolean,
        wrongAns: Boolean
    ): Int {
        return questions.count { question -> question.nonAnswers.equals(null) == nonAns }
    }

    //Return list of questions where wrong answer has been declared and count of questions is X.
    // If failed return 3 questions.
    override suspend fun getMyQuestions(
        ids: List<Int>?,
        nonAns: Boolean,
        wrongAns: Boolean,
        count: Int
    ): List<Question> {
        return listOf(questions
                .filter { question ->
//                    question.wrongAnswers.equals(null) == wrongAns and
//                    question.questionID.equals(ids) and
                    question.nonAnswers.equals(null)
                }
            .random() )
//                .random())
//                .subList(1, count-1)
//        } catch (e: Exception) {
//            Log.i("FakeQuestionRep", "getMyQuestions failed with: $e")
//            questions.subList(0,2)
//        }

    }

    override suspend fun getQuestionCount(topic_id: Int): Int {
        return questions.count { question -> question.topic == topic_id }
    }

    override suspend fun getQuestionCountFrom(topic_ids: List<Int>, noAns: Int): Int {
        return questions.count { question -> question.topic.equals(topic_ids.listIterator()) and (question.nonAnswers <= noAns) }
    }

    override fun getAllQuestions(): List<Question> {
        return questions
    }

    //Return random 10 questions
    override suspend fun getStartTest(): List<Question> {
        return listOf(questions.random()).subList(0,9)
    }

    override suspend fun getXQuestions(count: Int): List<Question> {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestionById(id: Int): Question? {
        return questions.find { it.questionID == id }
    }

    override suspend fun getXQuestionFromTopic(topic: Int, count: Int): List<Question> {
        TODO("Not yet implemented")
    }

    override suspend fun updateQuestion(question: Question) {
        questions.add(question)
    }

    override suspend fun updateTopics(topics: List<Topic>) {
        this.topics.addAll(topics)
    }

    override suspend fun getAllTopics(): List<Topic> {
        return topics
    }
}