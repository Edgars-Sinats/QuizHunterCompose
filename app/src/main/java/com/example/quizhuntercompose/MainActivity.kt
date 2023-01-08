package com.example.quizhuntercompose

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.presentation.TestPickScreen
//import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestScreen
import com.example.quizhuntercompose.ui.EntryPointActivity
import com.example.quizhuntercompose.ui.theme.QuizHunterComposeTheme
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

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

val question2 = Question(
    questionID= 2,
    question= "Lets try out in view how much we can expend this  question Question. Lets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question Question",
    answer1= "Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1.Question ans nbr. 1.",
    answer2= "2. Weary long text with no hidden meaning but only for test purposes. Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes.Weary long text with no hidden meaning but only for test purposes. ",
    answer3 = "3",
    answer4 = "Full question of all the stuff.",
    correctAnswer = 2,
    topic = 1,
    correctAnswers = 1, //make sure in initalizer it is = 0
    wrongAnswers = 1,
    nonAnswers = 0,
    averageAnswerTime = 0,
    lastAnswerTime = 0
)

@HiltAndroidApp
class QuizApp : Application() {
//    @Inject
//     var database: QuizDatabase
//    val database

    override fun onCreate() {
        super.onCreate()
//        database
        Log.i("Main:", "before create rec.")
//        database.init(applicationContext)
//        database.openHelper.writableDatabase

//        val database1 by lazy { QuizDatabase.getDatabase(this) }
//        Log.i("Main:", "database created - first question:" + database1.questionDao.getQuestionX(1).question)
//        createRecord(database1)
        Log.i("Main:", "record reacted")




    }
}

fun createRecord(database: QuizDatabase): Boolean{
//    database.questionDao.updateQuestion(question1)
//    database.questionDao.updateQuestion(question2)
    return true

}

//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            EntryPointActivity()
////            QuizHunterComposeTheme {
////                // A surface container using the 'background' color from the theme
////                Surface(
////                    modifier = Modifier.fillMaxSize(),
////                    color = MaterialTheme.colors.background
////                ) {
////                    TestPickScreen()
////                }
////            }
//        }
//    }
//}

@Composable
fun StartAct() {
    TestPickScreen()
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuizHunterComposeTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultScreenPreview() {
    QuizHunterComposeTheme {
        TestPickScreen()
    }
}