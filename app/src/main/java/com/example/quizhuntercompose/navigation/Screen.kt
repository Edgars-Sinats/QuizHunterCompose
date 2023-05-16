package com.example.quizhuntercompose.navigation

import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.cor.util.AppConstants.AUTH_SCREEN
import com.example.quizhuntercompose.cor.util.AppConstants.PROFILE_SCREEN
import com.example.quizhuntercompose.cor.util.AppConstants.QUIZ_PICK_SCREEN
import com.example.quizhuntercompose.cor.util.AppConstants.QUIZ_SCREEN
import com.example.quizhuntercompose.cor.util.AppConstants.REGISTER_SCREEN
import com.example.quizhuntercompose.cor.util.AppConstants.TAG_TEST_SCREEN
import com.example.quizhuntercompose.cor.util.AppConstants.TESTS_SCREEN

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: Int? = null
    ) {

    object ProfileScreen: Screen(
        route = PROFILE_SCREEN,
        title = PROFILE_SCREEN,
        icon = R.drawable.ic_baseline_user_24
    )

    //QUIZ
    object TestScreen: Screen(
        route = TAG_TEST_SCREEN,
        title = TAG_TEST_SCREEN,
        icon = R.drawable.ic_baseline_quiz_24
    )

    object TestsScreen: Screen(
        route = TESTS_SCREEN,
        title = TESTS_SCREEN,
        icon = R.drawable.ic_outline_list_24
    )

    object AuthScreen: Screen(AUTH_SCREEN)
    object RegisterScreen: Screen(REGISTER_SCREEN)
    object QuizPickScreen: Screen(QUIZ_PICK_SCREEN)
    object QuizScreen: Screen(QUIZ_SCREEN)

}
