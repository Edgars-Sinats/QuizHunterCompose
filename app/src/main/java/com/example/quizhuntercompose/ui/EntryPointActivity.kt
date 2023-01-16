package com.example.quizhuntercompose.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.quizhuntercompose.feature_pickTest.presentation.TestPickScreen
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestRoute
//import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestScreen
//import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestViewModel
import com.example.quizhuntercompose.ui.NavigationKeys.Arg.TEST_CATEGORY_ID
import com.example.quizhuntercompose.ui.theme.QuizHunterComposeTheme
import dagger.hilt.android.AndroidEntryPoint

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
    NavHost(navController, startDestination = NavigationKeys.Route.TEST_CHOOSE_SCREEN) {
        composable(route = NavigationKeys.Route.TEST_CHOOSE_SCREEN) {
            TestPickDestination(navController) // Choosing A Test
//            QuizTestDestination(navController) // Quiz screen - answering a test.
        }
        composable(
            route = NavigationKeys.Route.QUIZ_TEST_DETAILS,
            arguments = listOf(navArgument(NavigationKeys.Arg.TEST_CATEGORY_ID) {
                type = NavType.StringType
            })
        ) {
            QuizTestDestination(navController) // Quiz screen - answering a test.
//            TestPickDestination() // Choosing A Test

        }
    }
}

@Composable
private fun QuizTestDestination(navController: NavHostController) {
    TestRoute(
//        Modifier,
        navigateToFinish = { navController.navigate(NavigationKeys.Route.TEST_CHOOSE_SCREEN)
            } //TODO need to fix navigation on start choose quiz.
    )

//        effectFlow = viewModel.effects.receiveAsFlow(),
//        onNavigationRequested = { itemId ->
//            navController.navigate("${NavigationKeys.Route.FOOD_CATEGORIES_LIST}/${itemId}")
}


/*
    Chosing a test
 */
@Composable
private fun TestPickDestination(navController: NavHostController) {
    val viewModel: TestPickViewModel = hiltViewModel()

    TestPickScreen(
        viewModel,
        onNavigationRequested = {
            Log.i("EntryPointAct: ", "should go for QuizTestDet")

            navController.navigate(NavigationKeys.Route.QUIZ_TEST_DETAILS)
        }
    )
}

object NavigationKeys {

    object Arg {
        const val TEST_CATEGORY_ID = "testCategoryName"
    }

    object Route {
        const val TEST_CHOOSE_SCREEN = "test_categories_list"
        const val QUIZ_TEST_DETAILS = "$TEST_CHOOSE_SCREEN/{$TEST_CATEGORY_ID}"
    }

}

//Experimental Screen In one Activity.

