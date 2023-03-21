package com.example.quizhuntercompose.feature_pickTest.db.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.quizhuntercompose.MainDispatcherRule
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class QuestionDaoTest {

    private lateinit var database: QuizDatabase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            QuizDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertQuestionAndGetById() = runTest {
        // GIVEN - insert a task
        database.questionDao.updateQuestion(testQuestions[1])

        // WHEN - Get the question by id from the database
        val loaded = database.questionDao.getQuestionX(1)
        // THEN - The loaded data contains the expected values
        MatcherAssert.assertThat<Question>(loaded as Question, notNullValue())
        MatcherAssert.assertThat(loaded.questionID, `is`(testQuestions[1].questionID))
        MatcherAssert.assertThat(loaded.question, `is`(testQuestions[1].question))
        MatcherAssert.assertThat(loaded.topic, `is`(testQuestions[1].topic))
        MatcherAssert.assertThat(loaded.correctAnswer, `is`(testQuestions[1].correctAnswer))
        MatcherAssert.assertThat(loaded.answer1, `is`(testQuestions[1].answer1))
        MatcherAssert.assertThat(loaded.answer2, `is`(testQuestions[1].answer2))
        MatcherAssert.assertThat(loaded.answer3, `is`(testQuestions[1].answer3))
        MatcherAssert.assertThat(loaded.answer4, `is`(testQuestions[1].answer4))
        MatcherAssert.assertThat(loaded.correctAnswers, `is`(testQuestions[1].correctAnswers))
        MatcherAssert.assertThat(loaded.wrongAnswers, `is`(testQuestions[1].wrongAnswers))
        MatcherAssert.assertThat(loaded.nonAnswers, `is`(testQuestions[1].nonAnswers))
        MatcherAssert.assertThat(loaded.averageAnswerTime, `is`(testQuestions[1].averageAnswerTime))
        MatcherAssert.assertThat(loaded.lastAnswerTime, `is`(testQuestions[1].lastAnswerTime))
    }
}



//database.questionDao.updateQuestion(testQuestions[0])
//database.questionDao.updateQuestion(testQuestions[1])
//database.questionDao.updateQuestion(testQuestions[2])
//database.questionDao.updateQuestion(testQuestions[3])
private val testQuestions = listOf(
    Question(
        questionID= 0,
        question= "Example question 0 ",
        answer1= "Question ans nbr.",
        answer2= "2. Weary long text with no hidden meaning but only for test purposes. ",
        answer3 = "3",
        answer4 = "Small next preview",
        correctAnswer = 2,
        topic = 1,
        correctAnswers = 1, //make sure in initializer it is = 0
        wrongAnswers = 2,
        nonAnswers = 0,
        averageAnswerTime = 21,
        lastAnswerTime = 2
    ),
    Question(
        questionID= 1,
        question= "Example question 1 ",
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
        question= "Example question 2 ",
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
        question= "Example question 3 ",
        answer1= "Question ans nbr.",
        answer2= "2. Weary long text with no hidden meaning but only for test purposes. ",
        answer3 = "3",
        answer4 = null,
        correctAnswer = 5,
        topic = 2,
        correctAnswers = 1, //make sure in initializer it is = 0
        wrongAnswers = 0,
        nonAnswers = 2,
        averageAnswerTime = 30,
        lastAnswerTime = 10
    )
)