package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test

import com.example.quizhuntercompose.MainDispatcherRule
import com.example.quizhuntercompose.feature_pickTest.db.repository.FakeQuestionRepository
import com.example.quizhuntercompose.feature_pickTest.db.repository.TestQuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.QuizUseCase
import com.google.common.truth.Truth
import org.junit.Assert.*
//import org.mockito.Mockito.verify
import com.google.common.truth.Truth.assertThat //make run for 6.s
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import javax.inject.Inject

class TestPickViewModelTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepository = TestQuestionRepository()
    private lateinit var viewModel: TestPickViewModel

    @Before
    fun setup() {
        viewModel = TestPickViewModel(questionRepository = fakeRepository)
        //Could be good idea to load all questions/topic here instead off all testCases
    }

//    @Test
//    fun getx() = runTest {
//
//    }

    @Test
    fun getSetTopicNames() = runTest {
            assertTrue(fakeRepository.getAllTopics() == listOf<Topic>() )
            fakeRepository.updateTopics(listOf(Topic(1, "Topic1"), Topic(2,"Topic2")))
            assertTrue(fakeRepository.getAllTopics() == listOf(Topic(1, "Topic1"), Topic(2,"Topic2") ))
    }

    @Test
    fun getQuestionCount() = runTest {

        fakeRepository.updateQuestion(testQuestions[1])
        assertTrue(fakeRepository.getQuestionCountFrom(topic_ids = listOf(0,1,2,3), noAns = 1) == 1)
        assertEquals(fakeRepository.getQuestionCountFrom(topic_ids = listOf(0,1,2,3), noAns = 1) == 1, true)
        assertEquals( fakeRepository.getQuestionCount(1), 1) //Run for  6s and can`t load  Truth // After waiting for 60000 ms, the test coroutine is not completing
        //kotlinx.coroutines.test.UncompletedCoroutinesError: After waiting for 60000 ms, the test coroutine is not completing

//        fakeRepository.updateQuestion(testQuestions[2])

        assertNotNull( fakeRepository.getQuestionCount(1) )
        assertEquals( fakeRepository.getQuestionCountChecker(ids = listOf(1,2,3), nonAns = false, wrongAns = true) , 1)

        fakeRepository.updateQuestion(testQuestions[0])
        assertEquals( fakeRepository.getQuestionCountChecker(ids = listOf(1,2,3), nonAns = true, wrongAns = true), 2)
        assertEquals( fakeRepository.getQuestionCountChecker(ids = listOf(1,2,3), nonAns = false, wrongAns = false), 2)
        assertEquals( fakeRepository.getQuestionCountChecker(ids = listOf(1,2,3), nonAns = false, wrongAns = true), 2)
        fakeRepository.updateQuestion(testQuestions[3])
        assertEquals( fakeRepository.getQuestionCountChecker(ids = listOf(1,2,3), nonAns = true, wrongAns = true), 3)

    }

    @Test
    fun getMyQuestions() = runTest{
        assertEquals(fakeRepository.getMyQuestions(listOf(1,2), nonAns = false, wrongAns = true, count = 2), listOf<Question>() )
        fakeRepository.updateQuestion(testQuestions[0])
        fakeRepository.updateQuestion(testQuestions[1])
        fakeRepository.updateQuestion(testQuestions[2])
//        assertEquals(fakeRepository.getMyQuestions(listOf(1,2), nonAns = true, wrongAns = false, count = 2), listOf<Question>() )
//        assertEquals(fakeRepository.getMyQuestions(listOf(1,2), nonAns = true, wrongAns = true, count = 2),2)
//        assertEquals(fakeRepository.getMyQuestions(listOf(1,2), nonAns = false, wrongAns = true, count = 2),2)
//        assertEquals(fakeRepository.getMyQuestions(listOf(2,3), nonAns = true, wrongAns = true, count = 2),1)
        //TODO assertEquals(fakeRepository.getMyQuestions(listOf(2,3), nonAns = true, wrongAns = true, count = 0),0)
    }

}

private val testQuestions = listOf(
    Question(
        questionID= 0,
        question= "Lets try out this",
        answer1= "Question ans nbr.",
        answer2= "2. Weary long text with no hidden meaning but only for test purposes. ",
        answer3 = "3",
        answer4 = "Small next preview",
        correctAnswer = 2,
        topic = 1,
        correctAnswers = 1, //make sure in initializer it is = 0
        wrongAnswers = 0,
        nonAnswers = 0,
        averageAnswerTime = 21,
        lastAnswerTime = 2
    ),
    Question(
        questionID= 1,
        question= "Lets try out this",
        answer1= "Question ans nbr.",
        answer2= "2. Weary long text with no hidden meaning but only for test purposes. ",
        answer3 = "3",
        answer4 = "Small next preview",
        correctAnswer = 2,
        topic = 1,
        correctAnswers = 1, //make sure in initializer it is = 0
        wrongAnswers = 1,
        nonAnswers = 0,
        averageAnswerTime = 21,
        lastAnswerTime = 2
    ),
    Question(
        questionID= 2,
        question= "Lets try out this",
        answer1= "Question ans nbr.",
        answer2= "2. Weary long text with no hidden meaning but only for test purposes. ",
        answer3 = "3",
        answer4 = null,
        correctAnswer = 2,
        topic = 2,
        correctAnswers = 4, //make sure in initializer it is = 0
        wrongAnswers = 0,
        nonAnswers = 0,
        averageAnswerTime = 30,
        lastAnswerTime = 10
    ),
    Question(
        questionID= 3,
        question= "Lets try out this",
        answer1= "Question ans nbr.",
        answer2= "2. Weary long text with no hidden meaning but only for test purposes. ",
        answer3 = "3",
        answer4 = null,
        correctAnswer = 2,
        topic = 2,
        correctAnswers = 1, //make sure in initializer it is = 0
        wrongAnswers = 0,
        nonAnswers = 2,
        averageAnswerTime = 30,
        lastAnswerTime = 10
    )
)