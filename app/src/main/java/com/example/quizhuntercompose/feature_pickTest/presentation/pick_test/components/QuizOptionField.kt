package com.example.quizhuntercompose.feature_pickTest.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickEvent
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickOptionsState
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.TestViewModel

//fun QuizOptionsField() {
//}

@Composable
fun QuizOptionsField(
    modifier: Modifier = Modifier,
    testOptionState: TestPickOptionsState,
    onSwitchSelected: () -> Unit = {},
    checkedStateUnanswered: MutableState<Boolean>,
    checkedStateWrong: MutableState<Boolean>,
    listOfTopic: List<String>
) {
    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }
    val listOfTopic1 = arrayListOf(listOfTopic)
    val listOfTopic2: MutableState<List<String>> = remember {
        mutableStateOf(listOfTopic) //array list
    }

    val isSelected = rememberSaveable { mutableStateOf(listOfTopic[1]) }
    val contextForToast = LocalContext.current.applicationContext

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp) ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Unanswered",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding( horizontal = 16.dp)
            )
            Switch(
//            modifier = Modifier.align(End),
                checked = testOptionState.unanswered,
//                checkedStateUnanswered.value,
                onCheckedChange = {testOptionState.unanswered = !it}
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(
                text = "Wrongly answered",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Switch(
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(end = 16.dp),
                checked = checkedStateWrong.value,
                onCheckedChange = { checkedStateWrong.value = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        DropdownMenu(
            expanded = false,
            onDismissRequest = { expanded = false },
            ) {
            listOfTopic1.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    onClick = {
                        Toast.makeText(contextForToast, itemValue.toString(), Toast.LENGTH_SHORT).show()
                    }
//                enabled = (itemIndex != disabledItem)
                ) {
                    Text(text = itemValue.toString())
                }
            }
        }

    }

}

// TODO  Interactive + Bounce layout
@Composable
fun QuizOptionsField2(
    modifier: Modifier = Modifier,
//    selectedSwitchUnanswered: Boolean,
//    onSwitchUnansweredSelected: () -> Unit,
//    selectedSwitchWrong: Boolean,
//    onSwitchWrongSelected: () -> Unit,

    pickTestViewModel: TestPickViewModel,
//    uiState: TestPickOptionsState,
    onPickUnanswered: (TestPickEvent) -> Unit,
    onPickWrongAnswered: (TestPickEvent) -> Unit,
    onPickTime: (TestPickEvent) -> Unit,
    onTopicsSelected: (TestPickEvent) -> Unit, //Drop down or just scrollable list view ?
    onTestOptionState: (TestPickEvent) -> Unit,
    onCheckTopicQuestions: (TestPickEvent) -> Unit,

//    allTopics: List<String>,

    expanded: Boolean,
//    selectedSwitchTime: Boolean,
//    onSwitchTimeSelected: () -> Unit //Layout bottom.
//    viewModel: TestPickViewModel
){
    val uiState = pickTestViewModel.uiState.value
    var selectedTopics: List<String>
    val contextForToast = LocalContext.current.applicationContext
    var expanded1 by remember {
        mutableStateOf(expanded)
    }


    Column (
        modifier
            .fillMaxSize()
            .padding(8.dp),
    )  {

        Button(onClick = { expanded1 = !expanded1 }) {
            Text(text = "Open options")
        }
        Row() {
            Button(onClick = {onCheckTopicQuestions(TestPickEvent.CheckTopicQuestionCount(1)) } ) {
                Text(text = "Check topic 1 - ${uiState.count}")
            }

        }
        if (expanded1){


            Row(modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Unanswered")
                Switch(
                    checked = uiState.unanswered,
                    onCheckedChange = { onPickUnanswered(TestPickEvent.PickUnanswered(uiState.unanswered))} )
            }
            Row(modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Wrongly answered")
                Switch(
                    checked = uiState.unanswered,
                    onCheckedChange = { onPickWrongAnswered(TestPickEvent.PickWrongAnswered(uiState.wrongAnswersState))} )
            }
            Row(modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Time based / not yet")
                Switch(
                    checked = uiState.answerTime,
                    onCheckedChange = { onPickTime(TestPickEvent.PickTime(uiState.answerTime))} )
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn{
//            CheckBoxRow(checkedState = testOptionState.pickedAllTopic, onStateChange = {items(allTopics) {topic ->
//                CheckBoxRow(checkedState = false, onStateChange = { /*TODO*/ }, checkBoxText = topic) }
//            } , checkBoxText = "All topics")
                items(uiState.topics) { topic ->
                    CheckBoxRow(checkedState = false,
                        onStateChange = { selectedTopics = listOf("1","2","Weary long text .......xxxx........ of topics") },
                        checkBoxText = topic)
                }
            }
        }

//        Switch(checked = selectedSwitchWrong, onCheckedChange = {onSwitchWrongSelected})
//        Switch(checked = selectedSwitchUnanswered, onCheckedChange = {onSwitchUnansweredSelected})
    }
}

/*        DropdownMenu(
//            expanded = false,
//            onDismissRequest = { expanded1 = false },
//        ) {
//            allTopics.forEachIndexed { itemIndex, itemValue ->
//                DropdownMenuItem(
//                    onClick = {
//                        Toast.makeText(contextForToast, itemValue.toString(), Toast.LENGTH_SHORT).show()
//                    }
////                enabled = (itemIndex != disabledItem)
//                ) {
//                    Text(text = itemValue.toString())
//                }
//            }
        } */


@Composable
fun CheckBoxRow(
    modifier: Modifier = Modifier,
    checkedState: Boolean,
    onStateChange: () ->Unit,
    checkBoxText: String

    ){
//    val (checkedState, onStateChange) = remember { mutableStateOf(true) }
    Row(
        modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
//            .height(56.dp)
            .toggleable(
                value = checkedState,
                onValueChange = {
                    onStateChange()

                },
                role = androidx.compose.ui.semantics.Role.Checkbox
            ),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = checkBoxText,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = 16.dp)
        )
        Checkbox(
            checked = checkedState,
            onCheckedChange = null // null recommended for accessibility with screenreaders
        )
    }


}

@Preview
@Composable
fun CheckBoxRowPreview() {
    CheckBoxRow(checkedState = false, onStateChange = {  }, checkBoxText = "Some text of desc.")
}

@Preview
@Composable
fun QuizOptionsFieldPreview2(modifier: Modifier = Modifier) {
    QuizOptionsField2(
//        selectedSwitchUnanswered = false,
//        onSwitchUnansweredSelected = { /*TODO*/ },
//        selectedSwitchWrong = false,
//        onSwitchWrongSelected = { /*TODO*/ },
        expanded = true,
//        allTopics = listOf("1","2","Weary long text .......xxxx........ of topics", "","","So`ooo long topic list............ I don`t know how to put it in"),
        onTopicsSelected = { /*TODO*/ },
//        selectedSwitchTime = false,
//        onSwitchTimeSelected = { /*TODO*/ },
        onTestOptionState = {},
        pickTestViewModel = hiltViewModel(),
        onPickTime = {},
        modifier = Modifier,
        onPickUnanswered = {},
        onPickWrongAnswered = {},
        onCheckTopicQuestions = {}
    )
//        selectedTopics = listOf("1","2","Weary long text .......xxxx........ of topics")

//    }
}

@Preview
@Composable
fun QuizOptionsFieldPreview(modifier: Modifier = Modifier) {

    QuizOptionsField(
        modifier,
        onSwitchSelected = {},
        testOptionState = TestPickOptionsState(count = 5, pickedTopic = listOf("1","2"), pickedQuestions = emptyList(), pickedAllTopic = true,  questions = emptyList(), topics = emptyList(), isOptionsSectionVisible = true, answerTime = false, unanswered = false, wrongAnswersState = false ),
        checkedStateUnanswered = remember { mutableStateOf(true) },
        checkedStateWrong = remember { mutableStateOf(true) },
        listOfTopic = listOf("Topic 1", "Topic2", "Long topic 3, with even longer description of topic (your option)")
     )
}