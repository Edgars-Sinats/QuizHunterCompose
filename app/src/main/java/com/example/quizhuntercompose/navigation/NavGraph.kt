package com.example.quizhuntercompose.navigation

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.quizhuntercompose.cor.util.AppConstants
import com.example.quizhuntercompose.cor.util.AppConstants.QUIZ_OPTIONS
import com.example.quizhuntercompose.cor.util.AppConstants.TAG_NAV_GRAPH
import com.example.quizhuntercompose.feature_auth.presentation_auth.AuthScreen
import com.example.quizhuntercompose.feature_auth.presentation_profile.ProfileScreen
import com.example.quizhuntercompose.feature_pickTest.domain.model.TestOptions
import com.example.quizhuntercompose.feature_pickTest.presentation.TestPickScreen
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestScreen
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
//                    direction.navigateToProfileScreen
                }
            )
        }

        composable(
            route = Screen.ProfileScreen.route
        ) {
            ProfileScreen(
                navigateToAuthScreen = {
                    navController.popBackStack()
                    navController.navigate(Screen.AuthScreen.route)
//                    direction.navigateToAuthScreen
                },
                navigateToQuizPickScreen = {
                    navController.navigate(Screen.QuizPickScreen.route)
//                    direction.navigateToQuizPickScreen
                }
            )
        }

        composable(
            route = Screen.QuizPickScreen.route
        ){
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


//                    navController.previousBackStackEntry?.arguments?.putString("testPickView", userJson)
//                    navController.previousBackStackEntry?.savedStateHandle?.set("testPickView", userJson)
//                    navController.previousBackStackEntry?.savedStateHandle?.apply { ( set("testPickView", userJson) ) }
//
//                    navController.currentBackStackEntry?.savedStateHandle?.apply { set("testPickView", userJson) }
//                    navController.currentBackStackEntry?.arguments?.putString("testPickView", userJson)

//                    navController.navigate(NavigationKeys.Route.QUIZ_TEST_DETAILS.replace("{testCategoryName}", userJson))
//                    Log.i("NavGraph", "TestPickScreen Nav controller, " )
//                    direction.navigateToQuizScreen
                    navController.navigate("${Screen.QuizScreen.route}/$userJson" )
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
                        navController.navigate(Screen.QuizPickScreen.route)
                    },
                    startingQuestionArg = it
                )
            }

        }
    }
}