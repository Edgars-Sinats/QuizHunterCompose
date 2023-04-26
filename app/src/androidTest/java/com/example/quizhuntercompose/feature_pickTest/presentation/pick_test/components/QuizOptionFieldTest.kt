package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizhuntercompose.cor.util.TestTags
import com.example.quizhuntercompose.di.ApiModule
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
//import com.example.quizhuntercompose.di.AppModule
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.presentation.StepsSliderSample
import com.example.quizhuntercompose.feature_pickTest.presentation.TestPickContentSliderDetails
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickEvent
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.ui.EntryPointActivity
import com.example.quizhuntercompose.ui.theme.QuizHunterComposeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(ApiModule::class)
class QuizOptionFieldTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<EntryPointActivity>()
    private val activity get() = composeRule.activity

    @Inject
    lateinit var repository: QuestionRepository

    @Before
    fun setUp() {
        hiltRule.inject()
//        composeRule.setContent {
//            val navController = rememberNavController()
//            QuizHunterComposeTheme() {
//                NavHost(
//                    navController = navController,
//                    startDestination = NavigationKeys.Route.TEST_CHOOSE_SCREEN
//                ) {
//                    composable(route = NavigationKeys.Route.QUIZ_TEST_DETAILS) {
//                        TestPickScreen(){}
//
//                    }
//                }
//            }
//        }
    }

    @Test
    fun clickToggleOptionsSection_isVisible() {

        // WHEN - On startup
        setContentPickOptions()

        composeRule.onNodeWithTag(TestTags.PICK_QUIZ_OPTIONS_UNANSWERED_ROW).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.PICK_QUIZ_OPTIONS_ENABLER_BUTTON).performClick()
        composeRule.onNodeWithTag(TestTags.PICK_QUIZ_OPTIONS_UNANSWERED_ROW).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.PICK_QUIZ_OPTIONS_ENABLER_BUTTON).performClick()
        composeRule.onNodeWithTag(TestTags.PICK_QUIZ_OPTIONS_UNANSWERED_ROW).assertIsDisplayed()
    }

    @Test
    fun onSlideClick_show() {
        runBlocking { repository.updateQuestion(testQuestions[0])
            repository.updateQuestion(testQuestions[1])
            repository.updateQuestion(testQuestions[2])
            repository.updateQuestion(testQuestions[3])
            repository.updateQuestion(testQuestions[4])

            setConentStepSlideSample()
            composeRule.onNodeWithTag(TestTags.PICK_QUIZ_TEXT_VIEW_QUESTION_COUNT).assertIsDisplayed()
            composeRule.onNodeWithTag(TestTags.PICK_QUIZ_SLIDER).performTouchInput { swipeRight(startX = left, endX = right, durationMillis = 5500)  }
            composeRule.onNodeWithTag(TestTags.PICK_QUIZ_TEXT_VIEW_QUESTION_COUNT).assertIsDisplayed()
        }

//        setContentTestPick()


    }


    private fun setContentPickOptions() {
        val topicList : List<Topic> = listOf(Topic(0,"Zero topic"), Topic(1,"Second topic"), Topic(2,"New Topic"))
        val viewModle: TestPickViewModel = TestPickViewModel(repository)
        composeRule.setContent {
            QuizHunterComposeTheme {
                    QuizSelectOptionsTopics(
                        unanswered = TestPickEvent.PickUnanswered(true),
                        wronglyAnswered = TestPickEvent.PickWrongAnswered(true),
                        answerTime = TestPickEvent.PickTime(true),
                        questionCount = 3,
                        allTopicList = topicList,
                        selectedTopics = listOf(2,1),
                        checkAllTopics = TestPickEvent.CheckAllTopics(true),
                        onCheckAllTopics = {},
                        onTopicsSelected = {},
                        onPickUnanswered = {},
                        onPickWrongAnswered = {},
                        onPickTime = {},
                        onTestOptionState = {},
                        expanded = true
                    )
            }
        }
    }

    private fun setConentStepSlideSample(){
        val viewModel: TestPickViewModel = TestPickViewModel(repository)
        composeRule.setContent {
            QuizHunterComposeTheme() {
                StepsSliderSample(steps = viewModel.uiState.value.totalCount, maxSteps = viewModel.uiState.value.totalCount, currentSteps = viewModel.uiState.value.count, onSlide = viewModel::onEvent)
            }
        }
    }

    private fun setContentTestPick() {
        val viewModel: TestPickViewModel = TestPickViewModel(repository)

        composeRule.setContent {
            QuizHunterComposeTheme() {
                TestPickContentSliderDetails(
//                    onNavigation = { /*TODO*/ },
                    steps = viewModel.uiState.value.totalCount,
                    currentSteps = viewModel.uiState.value.count,
                    onSlide = {  }
                )
            }
        }
    }

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
            explanation = null,
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
            explanation = null,
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
            explanation = null,
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
            explanation = null,
            correctAnswers = 1, //make sure in initializer it is = 0
            wrongAnswers = 0,
            nonAnswers = 2,
            averageAnswerTime = 30,
            lastAnswerTime = 10
        ),

        Question(
            questionID = 4,
            question= "Example question 4 ",
            answer1= "Question ans nbr.",
            answer2= "2. Weary long text with no hidden meaning but only for test purposes. ",
            answer3 = "3",
            answer4 = null,
            correctAnswer = 5,
            topic = 2,
            explanation = null,
            correctAnswers = 1, //make sure in initializer it is = 0
            wrongAnswers = 0,
            nonAnswers = 2,
            averageAnswerTime = 30,
            lastAnswerTime = 10
        )
    )
}

