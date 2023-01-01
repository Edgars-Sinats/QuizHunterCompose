package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
// State flows after input event-> recomposition happens.
data class TestPickOptionsState(
    val questions: List<Question> = emptyList(),
    val topics: List<String> = emptyList()
) {
    var isOptionsSectionVisible: Boolean by mutableStateOf(false)
    var count by mutableStateOf<Int>(5) //Nbr of questions
    var unanswered by mutableStateOf(false)
    var wrongAnswersState by mutableStateOf(false)
    var answerTime by mutableStateOf (false) //Longest answered time
    var pickedAllTopic by mutableStateOf (true) //Checkbox from Answers {title}
    var pickedQuestions = listOf<Question>() //Taken Question form SQL OR in code level selects.
    var pickedTopic = mutableStateListOf<String>() //List of chosen topics.
}


//data class MultiCheckableItem (
//    val id: Int,
//    val name: String,
//    var checked: Boolean,
//    val level: Int,
//    val nestedItems: List<MultiCheckableItem>
//)
