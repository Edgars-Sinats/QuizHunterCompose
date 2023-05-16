package com.example.quizhuntercompose.navigation

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.quizhuntercompose.cor.util.AppConstants
import com.example.quizhuntercompose.cor.util.AppConstants.QUIZ_OPTIONS
import com.example.quizhuntercompose.cor.util.AppConstants.QUIZ_TEST_ID
import com.example.quizhuntercompose.cor.util.AppConstants.TAG_NAV_GRAPH
import com.example.quizhuntercompose.feature_auth.presentation_auth.AuthScreen
import com.example.quizhuntercompose.feature_auth.presentation_profile.ProfileScreen
import com.example.quizhuntercompose.feature_auth.presentation_register.RegisterScreen
import com.example.quizhuntercompose.feature_pickTest.domain.model.TestOptions
import com.example.quizhuntercompose.feature_pickTest.presentation.TestPickScreen
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestScreen
import com.example.quizhuntercompose.feature_tests.presentation.TestsScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


@Composable
@ExperimentalAnimationApi
fun NavGraph (
    navController: NavHostController
) {
//    val direction = remember ( navController ) { Direction ( navController ) }

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.ProfileScreen.route,
        enterTransition = { EnterTransition.None},
        exitTransition = { ExitTransition.None}
    ) {
        composable(
            route = Screen.ProfileScreen.route

        ) {
            ProfileScreen(

                navigateToAuthScreen = {
                    navController.navigate(Screen.AuthScreen.route)
                },
                navigateToQuizPickScreen = {
                    Log.i(TAG_NAV_GRAPH, "Pressed navigateToQuizPickScreens.")
                    navController.navigate(Screen.TestsScreen.route)
                }
            )
        }

        composable (
            route = Screen.AuthScreen.route
        ) {
            AuthScreen (
                navigateToProfileScreen = {
                    navController.popBackStack()
                    navController.navigate(Screen.ProfileScreen.route)
//                    navController.currentBackStackEntry.
//                    navController.backQueue.removeAll(navController.backQueue)
                },
                navigateToRegisterScreen = {
                    navController.navigate(Screen.RegisterScreen.route)
                }
            )
        }

        composable(
            route = Screen.RegisterScreen.route
        ) {
            RegisterScreen(
                navigateToSignInScreen = { navController.navigate( Screen.AuthScreen.route ) },
                popBackStack = { navController.popBackStack( Screen.AuthScreen.route, false) }
            )
        }

        composable(
            route = Screen.TestsScreen.route
        ) {

            TestsScreen(
                navigateToQuizPickScreen = {
                    navController.navigate("${Screen.QuizPickScreen.route}/$it")
                }
            )
        }

        composable(
            route = "${Screen.QuizPickScreen.route}/{$QUIZ_TEST_ID}",
            arguments = listOf( navArgument(QUIZ_TEST_ID){
                type = NavType.StringType
            })
        ){
            Log.i(TAG_NAV_GRAPH, "Navigating to TestPickScreen., Building TestPickViewModel")

            val viewModel: TestPickViewModel = hiltViewModel()

            TestPickScreen(
                viewModel,
                navigateToQuizScreen = { testId ->
                    Log.i(TAG_NAV_GRAPH, "navigateToQuizScreen pressed, building Moshi...")
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()

                    val jsonAdapter = moshi.adapter<TestOptions> (TestOptions::class.java)
                    val testOptionsObject = TestOptions (ids = viewModel.uiState.value.pickedTopicId, count = viewModel.uiState.value.count, viewModel.uiState.value.unanswered, viewModel.uiState.value.wrongAnswersState)
                    val userJson = jsonAdapter.toJson(testOptionsObject)

                    navController.popBackStack()
                    navController.navigate("${Screen.QuizScreen.route}/$userJson/$testId" ) {
                        popUpTo ( "${Screen.QuizScreen.route}/{$testId}" )
                    }
                },
                navigateToProfileScreen = {
                    navController.popBackStack()
                    navController.navigate(Screen.ProfileScreen.route)
                },
                test_id = QUIZ_TEST_ID
            )
        }
        composable(
            route = "${Screen.QuizScreen.route}/{$QUIZ_OPTIONS}/{$QUIZ_TEST_ID}",
            arguments = listOf(
                navArgument(QUIZ_OPTIONS) { type = NavType.StringType },
                navArgument(QUIZ_TEST_ID) { type = NavType.StringType }
            )
        ){ backStackEntry->
            Log.i(AppConstants.TAG_NAV_GRAPH, "start let...")
            // arguments is TestOptions
            val testId = backStackEntry.arguments?.getString(QUIZ_TEST_ID)
            backStackEntry.arguments?.getString(QUIZ_OPTIONS, "empty")?.let {
                Log.i(AppConstants.TAG_NAV_GRAPH, "let passed. arguments: ${ backStackEntry.arguments!!.getString("testPickView" ) } ")
                TestScreen(
                    navigateToFinish = {
                        navController.popBackStack()
                        navController.navigate("${Screen.QuizPickScreen.route}/{$testId}") {
                            popUpTo ( "${Screen.QuizPickScreen.route}/{$testId}" )
                        }
//                        navController.popBackStack(route = Screen.QuizPickScreen.route, inclusive = false)
                    },
                    startingQuestionArg = it
                )
            }

        }
    }
}