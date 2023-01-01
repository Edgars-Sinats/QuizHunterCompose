package com.example.quizhuntercompose.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.presentation.TestPickScreen
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestRoute
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestScreen
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestViewModel
import com.example.quizhuntercompose.ui.NavigationKeys.Arg.TEST_CATEGORY_ID
import com.example.quizhuntercompose.ui.theme.QuizHunterComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
val question1 = Question(
    questionID= 1,
    question= "Lets try out in view how much we can expend this  question Question. Lets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question Question",
    answer1= "Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1.Question ans nbr. 1.",
    answer2= "2. Weary long text with no hidden meaning but only for test purposes. Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes. ",
    answer3 = "3",
    answer4 = null,
    correctAnswer = 2,
    topic = 1,
    correctAnswers = 1, //make sure in initalizer it is = 0
    wrongAnswers = 1,
    nonAnswers = 0,
    averageAnswerTime = 21,
    lastAnswerTime = 2
)
*/



// Single Activity per app
@AndroidEntryPoint
class EntryPointActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val db = QuizDatabase.getDatabase(this)

/*   Does db has problem? - NO All good.
        val db = Room.databaseBuilder(
//            applicationContext,
//            QuizDatabase::class.java,
//            "hunt1Question"
//        ).createFromAsset("huntQuestion.db")
//            .build()
//        db = QuizDatabase.getDatabase(this)

//        var text: String = "nullito"
//        CoroutineScope(Dispatchers.Default).launch {
////            db.openHelper.writableDatabase // why can`t copy?
////            db.query("SELECT * FROM " + Room.MASTER_TABLE_NAME, null)
////            db.questionDao.updateQuestion(
////                    question1
////                )
//            if (db.questionDao.getQuestionX(4)!= null){
//                text = db.questionDao.getQuestionX(4).question
//            }
//        }
*/

        setContent {
            QuizHunterComposeTheme {
//                QuizHelloApp(text)
                QuizHunterApp1()
            }
        }
    }
}

@Composable
private fun QuizHunterApp1() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.TEST_CATEGORIES_LIST) {
        composable(route = NavigationKeys.Route.TEST_CATEGORIES_LIST) {
            QuizTestDestination(navController) // Quiz screen - answering a test.
        }
        composable(
            route = NavigationKeys.Route.QUIZ_TEST_DETAILS,
            arguments = listOf(navArgument(NavigationKeys.Arg.TEST_CATEGORY_ID) {
                type = NavType.StringType
            })
        ) {
            TestPickDestination() // Choosing A Test
        }
    }
}

@Composable
private fun QuizTestDestination(navController: NavHostController) {
/*      viewModel = hilt // OR uiState
//    var viewModel: TestViewModel = hiltViewModel()
//    val uiState by viewModel.uiState.collectAsState()
//if (uiState.answers.isNotEmpty() ){
//    Log.i("EntryPoint: ", "uiState answer: " + uiState.answers[uiState.currentQuestionIndex].chosenAnswer.toString())
//    Log.i("EntryPoint: ", "viewModel" + viewModel.uiState.collectAsState().value.answers[1].chosenAnswer)
//
//}
//    viewModel.on
*/
    TestRoute(
//        Modifier,
        navigateToFinish = { navigateTo -> navController.findDestination(NavigationKeys.Route.QUIZ_TEST_DETAILS) } //TODO need to fix navigation on start choose quiz.
    )

//        effectFlow = viewModel.effects.receiveAsFlow(),
//        onNavigationRequested = { itemId ->
//            navController.navigate("${NavigationKeys.Route.FOOD_CATEGORIES_LIST}/${itemId}")
}


@Composable
private fun TestPickDestination() {
    val viewModel: TestPickViewModel = hiltViewModel()
    TestPickScreen()
}

object NavigationKeys {

    object Arg {
        const val TEST_CATEGORY_ID = "testCategoryName"
    }

    object Route {
        const val TEST_CATEGORIES_LIST = "food_categories_list"
        const val QUIZ_TEST_DETAILS = "$TEST_CATEGORIES_LIST/{$TEST_CATEGORY_ID}"
    }

}

//Experimental Screen In one Activity.

