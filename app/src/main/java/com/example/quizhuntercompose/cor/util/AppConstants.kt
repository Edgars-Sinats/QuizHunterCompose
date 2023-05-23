package com.example.quizhuntercompose.cor.util

object AppConstants {

    //App
    const val TAG = "QuizHunter"
    const val TAG_NAV_GRAPH = "NavGraph"
    const val TAG_TEST_SCREEN = "TestScreen"
    const val TAG_TEST_VIEW_MODEL = "TestViewModel"

    //Firebase
    const val USER_COLLECTION = "users"
    const val QUESTION_COLLECTION = "questions"
    const val TEST_COLLECTION = "tests"
    const val ANSWER_COLLECTION = "user_answers"


    //Names
    const val SIGN_IN_REQUEST = "signInRequest"
    const val SIGN_UP_REQUEST = "signUpRequest"

    //Buttons
    const val SIGN_IN_WITH_GOOGLE = "Sign in with Google"
    const val SIGN_OUT = "Sign-out"
    const val REVOKE_ACCESS = "Revoke Access"
    const val NEXT = "Next"
    const val PREVIOUS = "Previous"
    const val DONE = "Done"
    const val SKIP = "SKIP"
    const val START_QUIZ = "Start Quiz"
    const val BACK_TO_HOME = "Back to home"
    const val CREATE_ACCOUNT = "Create Account,"
    const val SIGN_UP_TO_GET_STARTED = "Register to get access to all features of \n Quiz Hunter."
    const val USERNAME = "Username"
    const val EMAIL = "Email"
    const val PASSWORD = "Password"


    //Screens
    const val AUTH_SCREEN = "Authentication"
    const val REGISTER_SCREEN = "Registration"
    const val TESTS_SCREEN = "Tests"
    const val QUIZ_PICK_SCREEN = "Quiz Pick"
    const val QUIZ_SCREEN = "Quiz"
    const val PROFILE_SCREEN = "Profile"

    const val MAIN_SCREEN = "Main" //TODO Quiz catalog - hunter - new every day -

    const val WELCOME_QUIZ_HUNTER = "Welcome to/QuizHunter"
    const val SIGN_IN_TO_ACCESS = "Login to see your tests."



    //Arguments
//    const val QUIZ_ID = "quizId" //testID
    const val QUIZ_OPTIONS = "quizOptions"
    const val QUIZ_TEST_ID = "quizTestId"
    const val QUIZ_TEST_LANGUAGE = "quizLanguage"


    //Messages
    const val REVOKE_ACCESS_MESSAGE = "You need to re-authenticate before revoking the access."

    //Helper
    const val EMAIL_REGEX = "^[^\\s;]+@[^\\s;]+\\.[^\\s;]+(?:;[^\\s;]+@[^\\s;]+\\.[^\\s;]+)*\$"


}