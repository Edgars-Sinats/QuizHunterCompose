package com.example.quizhuntercompose.feature_pickTest.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickEvent

// TODO  Interactive + Bounce layout
@Composable
fun QuizOptionsField2(
    modifier: Modifier = Modifier,
    unanswered: TestPickEvent.PickUnanswered,
    wronglyAnswered: TestPickEvent.PickWrongAnswered,
    answerTime: TestPickEvent.PickTime,
    checkedTopics: (TestPickEvent) -> Unit,
    allTopicQuestions: List<Topic>,

    onPickUnanswered: (TestPickEvent) -> Unit,
    onPickWrongAnswered: (TestPickEvent) -> Unit,
    onPickTime: (TestPickEvent) -> Unit,
    onTopicsSelected: (TestPickEvent) -> Unit, //Drop down or just scrollable list view ?
    onTestOptionState: (TestPickEvent) -> Unit,
    expanded: Boolean,
){
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
            Button(onClick = {checkedTopics(TestPickEvent.CheckTopicQuestionCount(1)) } ) {
                Text(text = "Check topic 1 - ${allTopicQuestions[1].topic}")
            }

        }
        if (expanded1){


            Row(modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Unanswered")
                Switch(
                    checked = unanswered.value,
                    onCheckedChange = { onPickUnanswered(TestPickEvent.PickUnanswered(unanswered.value))} )
            }
            Row(modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Wrongly answered")
                Switch(
                    checked = wronglyAnswered.value,
                    onCheckedChange = { onPickWrongAnswered(TestPickEvent.PickWrongAnswered(wronglyAnswered.value))} )
            }
            Row(modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Time based / not yet")
                Switch(
                    checked = answerTime.value,
                    onCheckedChange = { onPickTime(TestPickEvent.PickTime(answerTime.value))} )
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn{
//            CheckBoxRow(checkedState = testOptionState.pickedAllTopic, onStateChange = {items(allTopics) {topic ->
//                CheckBoxRow(checkedState = false, onStateChange = { /*TODO*/ }, checkBoxText = topic) }
//            } , checkBoxText = "All topics")
//                items(allTopicQuestions) { topic ->
//                    CheckBoxRow(checkedState = false,
//                        onStateChange = {
//                            topic.selectedTopics = listOf("1","2","Weary long text .......xxxx........ of topics") },
//                        checkBoxText = topic.toString())
//                }
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
//@Preview("TestPick - QuizOptionsFieldPreview2" , uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QuizOptionsFieldPreview2(modifier: Modifier = Modifier) {
    QuizOptionsField2(
        expanded = true,
        onTopicsSelected = { /*TODO*/ },
        onTestOptionState = {},
        onPickTime = {},
        modifier = Modifier,
        onPickUnanswered = {},
        onPickWrongAnswered = {},
        allTopicQuestions = emptyList(),
        wronglyAnswered = TestPickEvent.PickWrongAnswered(false),
        answerTime = TestPickEvent.PickTime(true),
        unanswered = TestPickEvent.PickUnanswered(true),
        checkedTopics = {}
    )
}