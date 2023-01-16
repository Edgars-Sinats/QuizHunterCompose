package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
// State flows after input event-> recomposition happens.
data class TestPickOptionsState(

    var questions: List<Question> = emptyList(), //All Questions? TODO How is the performance with all of them?
    val topics: List<String> = emptyList(),
    val isOptionsSectionVisible: Boolean,
    var count: Int = 0, //Nbr of questions

    var unanswered: Boolean = false,
    val wrongAnswersState: Boolean = false,
    val answerTime: Boolean = false, //Longest answered time

    val pickedAllTopic: Boolean = true, //Checkbox from Answers {title}
    val pickedQuestions: List<Question>, //Taken Question form SQL OR in code level selects.
    val pickedTopic: List<String> //List of chosen topics.
) {

}


//data class MultiCheckableItem (
//    val id: Int,
//    val name: String,
//    var checked: Boolean,
//    val level: Int,
//    val nestedItems: List<MultiCheckableItem>
//)
