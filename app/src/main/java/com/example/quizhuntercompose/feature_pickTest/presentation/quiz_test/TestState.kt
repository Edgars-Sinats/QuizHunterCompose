package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question

/**
 * [showPreview] value is true after [TestState] is submitted and loaded to db.
 * Preview of result is show and user can browse throw his answers and see mistakes.
 *
 * [correctAnswerCount] is total
 *
 * TODO  as we use [showDialog] now for showing results on updateQuestion(). We should show dialog only once and after browsing results using [showPreview] we should return to main page without second time popUpDialog opening.
 *
 */

 data class TestState(
     val questionStateList: List<QuestionState> = emptyList(),
     var answers: List<Answer> = emptyList(),
     val answerTime: Boolean = false, //TODO In the test you have 1 min for 1 question? (Optional show tme progress bar - BUT need to be in Test level not question level) Implement time progress bar.
     var showPreview: Boolean,
     val currentQuestionIndex : Int = 0,
     val correctAnswerCount: Int = 0,
     val wrongAnswerCount: Int = 0,
     val showDialog: Boolean = false
//On back press -> back -> done. Save results if not answered fully // If null for time stamp - don`t change/save question in answer table.
){
}

/**
*  [chosenAnswer] parameter is already answered and ready to submit to db.
 *  In data class [Answer] - [chosenAnswer] are only pressed from user but not "submitted".
 */
data class QuestionState(
    val question: Question,
    var chosenAnswer: Int?,
    val questionStateId: Int
//On back press -> back -> done. Save results if not answered fully // If null for time stamp - don`t change/save question in answer table.

) {
//    var currentQuestionIndex by mutableStateOf(0) //In Test, on which question state we are currently stopped. (1..last)
    var stateAnswerList =  mutableListOf<Answer>() //Answered question list. After submit q., answer list update, after skip, answer list update with =+ time stamp
}

data class Answer(
    val questionId: Int,
    var chosenAnswer: Int? = null, //TODO 60. chosenAns in layout (before saving)?
    var timeSpent: Long? = null,   //Time spend in milliseconds.
    val isLastQuestion: Boolean,
    val isFirstQuestion: Boolean
)

//sealed class TestState(){
//
//    data class QuestionsFromState(
//        val answerTime: Boolean,
//        val questionState: List<QuestionState>
//    ) : TestState() {
//        var currentQuestionIndex by mutableStateOf(0)
//    }
//
//    data class AnswersFromState(
//        val answerState: List<Answer>
//    ) : TestState(){
//        var currentQuestionIndex by mutableStateOf(0)
//    }
//}

