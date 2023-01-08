package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question

 data class TestState(
     val questionStateList: List<QuestionState> = emptyList(),
     var answers: List<Answer> = emptyList(),

     val answerTime: Boolean = false, //TODO In the test you have 1 min for 1 question? (Optional show tme progress bar - BUT need to be in Test level not question level)Implement time progress bar
//     val showPrevious: Boolean = false, //Back/Atpakal
//     val showDone: Boolean = false, // Submit/Iesniegt/Apstiprinat
//     val showNext:Boolean = true,// Skip/Izlaist
    var showPreview: Boolean,
     val currentQuestionIndex : Int = 0
//On back press -> back -> done. Save results if not answered fully // If null for time stamp - don`t change/save question in answer table.
){
}

data class QuestionState(
    val question: Question,
//    val showPrevious: Boolean, //Back/Atpakal
//    val showDone: Boolean, // Submit/Iesniegt/Apstiprinat
//    val showNext:Boolean,// Skip/Izlaist
    var chosenAnswer: Int?, //TODO 60. Saved question ans?
    val questionStateId: Int
//    val isLastQuestionInTest: Boolean = false
//    val showPreview: Boolean
//On back press -> back -> done. Save results if not answered fully // If null for time stamp - don`t change/save question in answer table.

) {
//    var currentQuestionIndex by mutableStateOf(0) //In Test, on which question state we are currently stopped. (1..last)
    var stateAnswerList =  mutableListOf<Answer>() //Answered question list. After submit q., answer list update, after skip, answer list update with =+ time stamp
}

data class Answer(
    val questionId: Int,
    var chosenAnswer: Int? = null, //TODO 60. chosenAns in layout (before saving)?
    var timeSpent: Int? = null,   //Time spend in milliseconds.
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

