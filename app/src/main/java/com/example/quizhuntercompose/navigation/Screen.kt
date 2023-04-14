package com.example.quizhuntercompose.navigation

import com.example.quizhuntercompose.cor.util.AppConstants.AUTH_SCREEN
import com.example.quizhuntercompose.cor.util.AppConstants.PROFILE_SCREEN
import com.example.quizhuntercompose.cor.util.AppConstants.QUIZ_PICK_SCREEN
import com.example.quizhuntercompose.cor.util.AppConstants.QUIZ_SCREEN

sealed class Screen(val route: String) {
    object AuthScreen: Screen(AUTH_SCREEN)
    object QuizScreen: Screen(QUIZ_SCREEN)
    object QuizPickScreen: Screen(QUIZ_PICK_SCREEN)
    object ProfileScreen: Screen(PROFILE_SCREEN)

}
