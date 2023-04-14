package com.example.quizhuntercompose.navigation

import android.util.Log
import androidx.navigation.NavHostController

class Direction(
    navController: NavHostController
) {
    val navigateToQuizPickScreen: () -> Unit = {
        navController.navigate(Screen.QuizPickScreen.route)
    }

    val navigateToAuthScreen: () -> Unit = {
        navController.popBackStack()
        navController.navigate(Screen.AuthScreen.route)
    }

    val navigateToQuizScreen: () -> Unit = {
        Log.i("Directions", "Navigating to QuizScreen")
        navController.navigate(Screen.QuizScreen.route)
    }

    val navigateToProfileScreen: () -> Unit = {
        navController.navigate(Screen.ProfileScreen.route)
        Log.i("Directions", "Navigating to ProfileScreen")
    }

    val navigateBack: () -> Unit = {
        navController.navigateUp()
    }
}