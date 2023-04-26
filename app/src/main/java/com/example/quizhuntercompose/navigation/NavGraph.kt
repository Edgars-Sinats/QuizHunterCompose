package com.example.quizhuntercompose.navigation

import android.util.Log
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.quizhuntercompose.cor.util.AppConstants
import com.example.quizhuntercompose.cor.util.AppConstants.QUIZ_OPTIONS
import com.example.quizhuntercompose.cor.util.AppConstants.TAG_NAV_GRAPH
import com.example.quizhuntercompose.feature_auth.presentation_auth.AuthScreen
import com.example.quizhuntercompose.feature_auth.presentation_profile.ProfileScreen
import com.example.quizhuntercompose.feature_pickTest.domain.model.TestOptions
import com.example.quizhuntercompose.feature_pickTest.presentation.TestPickScreen
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestScreen
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.popUpDialogPreview
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


@Composable
@ExperimentalAnimationApi
fun NavGraph (
    navController: NavHostController
) {
    val direction = remember ( navController ) { Direction ( navController ) }

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.AuthScreen.route,
        enterTransition = { EnterTransition.None},
        exitTransition = { ExitTransition.None}
    ) {
        composable (
            route = Screen.AuthScreen.route
        ) {
            AuthScreen (
                navigateToHomeScreen = {
                    navController.navigate(Screen.ProfileScreen.route)
//                    navController.currentBackStackEntry.

//                    navController.backQueue.removeAll(navController.backQueue)
//                    direction.navigateToProfileScreen
                }
            )
        }

        composable(
            route = Screen.ProfileScreen.route

        ) {
            ProfileScreen(

                navigateToAuthScreen = {
                    navController.navigate(Screen.AuthScreen.route)
                },
                navigateToQuizPickScreen = {
//                    navController.popBackStack()
                    Log.i(TAG_NAV_GRAPH, "Pressed navigateToQuizPickScreen.")

                    navController.navigate(Screen.QuizPickScreen.route)
//                    direction.navigateToQuizPickScreen
                }
            )
        }

        composable(
            route = Screen.QuizPickScreen.route
        ){
            Log.i(TAG_NAV_GRAPH, "Navigating to TestPickScreen., Building TestPickViewModel")

            val viewModel: TestPickViewModel = hiltViewModel()

            TestPickScreen(
                viewModel,
                navigateToQuizScreen = {
                    Log.i(TAG_NAV_GRAPH, "navigateToQuizScreen pressed, building Moshi...")
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()

                    val jsonAdapter = moshi.adapter<TestOptions> (TestOptions::class.java)
                    val testOptionsObject = TestOptions (ids = viewModel.uiState.value.pickedTopicId, count = viewModel.uiState.value.count, viewModel.uiState.value.unanswered, viewModel.uiState.value.wrongAnswersState)
                    val userJson = jsonAdapter.toJson(testOptionsObject)

                    navController.navigate("${Screen.QuizScreen.route}/$userJson" )
                },
                navigateToProfileScreen = {
                    navController.popBackStack()
                    navController.navigate(Screen.ProfileScreen.route)
                }
            )
        }
        composable(
            route = "${Screen.QuizScreen.route}/{$QUIZ_OPTIONS}",
            arguments = listOf(navArgument(QUIZ_OPTIONS) {
                type = NavType.StringType
            })
        ){ backStackEntry->
            Log.i(AppConstants.TAG_NAV_GRAPH, "start let...")
            backStackEntry.arguments?.getString(QUIZ_OPTIONS, "empty")?.let {
                Log.i(AppConstants.TAG_NAV_GRAPH, "let passed. arguments: ${ backStackEntry.arguments!!.getString("testPickView" ) } ")
                TestScreen(
                    navigateToFinish = {
        //                    direction.navigateToQuizPickScreen
//                        navController.popBackStack()
//                        navController.navigate(Screen.QuizPickScreen.route)


                        navController.popBackStack()
                        navController.navigate(Screen.QuizPickScreen.route) { popUpTo ( Screen.QuizPickScreen.route ) }
//                        navController.popBackStack(route = Screen.QuizPickScreen.route, inclusive = false)
                    },
                    startingQuestionArg = it
                )
            }

        }
    }
}