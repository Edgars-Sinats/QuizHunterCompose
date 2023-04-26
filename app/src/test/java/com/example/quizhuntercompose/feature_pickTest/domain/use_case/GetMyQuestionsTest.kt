package com.example.quizhuntercompose.feature_pickTest.domain.use_case

import android.util.Log
import com.google.common.truth.Truth.assertThat

import com.example.quizhuntercompose.feature_pickTest.db.repository.FakeQuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetMyQuestionsTest {

    private lateinit var getMyQuestions: GetMyQuestions
    private lateinit var getMyTopic: GetAllTopics
    private lateinit var fakeRepository: FakeQuestionRepository

    @Before
    fun setUp() {
//        Log.i("GetMyQuestionsTest", "Set up questions : ")
        fakeRepository = FakeQuestionRepository()
        getMyTopic = GetAllTopics(fakeRepository)
        getMyQuestions = GetMyQuestions(fakeRepository)


        val questionsToInsert = mutableListOf<Question>()
        ('a'..'z').forEachIndexed { index, c ->
//            Log.i("GetMyQuestionsTest", "Question $index : " + c.toString())

            questionsToInsert.add(
                Question(
                    question = c.toString(),
                    answer1 = c.toString(),
                    answer2 = c.toString(),
                    answer3 = c.toString(),
                    answer4 = c.toString(),
                    correctAnswers = index,
                    questionID = index,
                    topic = index,
                    explanation = null,
                    correctAnswer = index,
                    wrongAnswers = index+1,
                    nonAnswers = index+3,
                    lastAnswerTime = index*2,
                    averageAnswerTime = (index * 19 +2)
                )
            )
        }

        questionsToInsert.shuffle()
        runBlocking {
            questionsToInsert.forEach { fakeRepository.updateQuestion(it) }
        }
    }

    @Test
    fun `Order question by question ascending correct order`() = runBlocking{
        val questions = getMyQuestions(count = 11, ids = listOf(1,3,2,4,5), nonAns = true, wrongAns = false)


        for (i in 0 until questions.size-1) {
//            Log.i("GetMyQuestionsTest", "Question $i : " + questions[i])
//            assertThat(questions.get(i)?.topic).isAnyOf(4,2)
            assertThat(questions.get(i)?.nonAnswers).isNotEqualTo(0)
//            assertTh(questions[i].topic)
        }
    }

//    @Test
//    fun `Test Topics load`() = runBlocking {
//        val topics = getMyTopic()
//
//        for (i in 0 .. topics.size) {
//            assertThat(topics[i])
//        }
//    }
}