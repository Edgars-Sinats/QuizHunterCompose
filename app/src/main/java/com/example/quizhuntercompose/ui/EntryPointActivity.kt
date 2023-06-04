package com.example.quizhuntercompose.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizhuntercompose.feature_auth.presentation_auth.AuthViewModel
import com.example.quizhuntercompose.feature_pickTest.domain.model.TestOptions
import com.example.quizhuntercompose.feature_pickTest.presentation.TestPickScreen
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestScreen
import com.example.quizhuntercompose.navigation.NavGraph
import com.example.quizhuntercompose.navigation.Screen
import com.example.quizhuntercompose.ui.NavigationKeys.Arg.TEST_CATEGORY_ID
import com.example.quizhuntercompose.ui.theme.QuizHunterComposeTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class EntryPointActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberAnimatedNavController()
            val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current


            QuizHunterComposeTheme{
                NavGraph(
                    navController = navController
                )
            }

            checkAuthState()
        }
    }

    override fun onBackPressed() {
        if (navController.currentBackStackEntry == null){
            super.onBackPressed()
            finish()

        }else {
            navController.popBackStack()
        }
    }

    private fun checkAuthState() {
        if(viewModel.isAuthenticated) {
            return
//            navigateToQuizPickScreen()
        }
    }

    private fun navigateToQuizPickScreen() = navController.navigate(Screen.QuizPickScreen.route)
}

@Composable
private fun QuizHunterApp1() {
    Log.i("EntryPointAct: ", "On QuizHunterApp Build")
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

/*
    Quiz Test.
 */
@Composable
private fun QuizTestDestination(navController: NavHostController) {
    TestScreen(
//        navCont = navController,
        navigateToFinish = { navController.navigate(NavigationKeys.Route.TEST_CHOOSE_SCREEN) },
        startingQuestionArg = "navArgument from composable()"
    )

//        effectFlow = viewModel.effects.receiveAsFlow(),
//        onNavigationRequested = { itemId ->
//            navController.navigate("${NavigationKeys.Route.FOOD_CATEGORIES_LIST}/${itemId}")
}



/*
    Choosing a test. Quiz Settings.
 */
@Composable
private fun TestPickDestination(navController: NavHostController) {
    Log.i("EntryPointAct: ", "Before TestPickViewModel build")
    val viewModel: TestPickViewModel = hiltViewModel()

    TestPickScreen(
        viewModel,
        navigateToQuizScreen = {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val jsonAdapter = moshi.adapter<TestOptions>(TestOptions::class.java)
            //TODO check
            val testOptionsObject = TestOptions(ids = viewModel.uiState.value.pickedTopicId, count = viewModel.uiState.value.count, viewModel.uiState.value.unanswered, viewModel.uiState.value.wrongAnswersState, viewModel.uiState.value.pickedTestId, viewModel.uiState.value.answerTime)
            val userJson = jsonAdapter.toJson(testOptionsObject)

            // TODO pass to testViewModel. => viewModel.questionStateList
            navController.currentBackStackEntry?.savedStateHandle?.set("testPickView", userJson)
            navController.previousBackStackEntry?.arguments?.putString("Test", userJson)

            navController.navigate(NavigationKeys.Route.QUIZ_TEST_DETAILS.replace("{testCategoryName}", userJson))
            Log.i("EntryPointAct", "picketTopicIds: ${viewModel.uiState.value.pickedTopicId} and Test1 totalCount: ${viewModel.uiState.value.totalCount}")
        },
        navigateToProfileScreen = {
            navController.navigate(Screen.ProfileScreen.route)
        },
        test_id = "1"
    )
}

object NavigationKeys {

    object Arg {
        const val TEST_CATEGORY_ID = "testCategoryName"
        val TEST_QUESTIONS_DETAILS = "questions-details/{questions}"
    }

    object Route {
        const val TEST_CHOOSE_SCREEN = "test_categories_list"
        const val QUIZ_TEST_DETAILS = "$TEST_CHOOSE_SCREEN/{$TEST_CATEGORY_ID}"
    }

}

//Experimental Screen In one Activity.

