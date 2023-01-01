package com.example.quizhuntercompose.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.quizhuntercompose.ui.HunterComposeScreen.HOME_SCREEN
import com.example.quizhuntercompose.ui.HunterComposeScreen.TEST_PICK_SCREEN
import com.example.quizhuntercompose.ui.HunterComposeScreen.TEST_SCREEN
import com.example.quizhuntercompose.ui.HunterDestinationsArgs.QUIZ_USE_CASE_ARG

//import androidx.navigation.compose.rememberNavController

/**
 * Destinations used in the [HunterComposeApp].
 */
private object HunterComposeScreen {
    const val HOME_SCREEN = "home"  //TODO
    const val TEST_PICK_SCREEN = "testPick"
    const val TEST_SCREEN = "test"
}

/**
 * Arguments used in [TodoDestinations] routes
 */
object HunterDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val TASK_ID_ARG = "taskId"
    const val QUIZ_USE_CASE_ARG = "useCase"
}

/**
 * Destinations used in the [TasksActivity]
 */
object HunterComposeDestinations {
    const val TEST_ROUTE = "$TEST_SCREEN/{$QUIZ_USE_CASE_ARG}"
    const val HOME_ROUTE = HOME_SCREEN
//    const val TEST_DETAIL_ROUTE = "$TASK_DETAIL_SCREEN/{$TASK_ID_ARG}"
    const val TEST_PICK_ROUTE = TEST_PICK_SCREEN
}

/**
 * Models the navigation actions in the app.
 */
class QuizHunterNavigationAction(private val navController: NavHostController) {
    val navigateToTestPick: () -> Unit = {
        navController.navigate(HunterComposeDestinations.TEST_PICK_ROUTE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations //TODO change
            // on the back stack as users select test
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
    val navigateToTestQuiz: () -> Unit = {
        navController.navigate(HunterComposeDestinations.TEST_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

//class QuizHunterNavigationAction1(navController: NavHostController) {
//    val navigateToHome: () -> Unit {
//        navController.navigate(HunterComposeDestinations.TEST_ROUTE){
//            popUpTo(navController.graph.findStartDestination().id) {
//                savedState = true
//            }
//            launchSingleTop = true
//            restoreState = true
//        }
//    }
//}

//class QuizHunterNavigationAction(navController: NavHostController) {
//    val navigateToHome: () -> Unit {
//
//        navigate(HunterComposeDestinations.HOME_ROUTE){
//            popUpTo(rememberNavController.graph.findStartDestination().id){
//                saveState = true
//            }
//
//        }
//    }
//}