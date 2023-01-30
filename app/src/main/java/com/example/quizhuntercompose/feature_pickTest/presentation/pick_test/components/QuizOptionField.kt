package com.example.quizhuntercompose.feature_pickTest.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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
    questionCount: Int,
    allTopicList: List<Topic>,
    selectedTopics: List<Int>,
    checkAllTopics: TestPickEvent.CheckAllTopics,
    onCheckAllTopics: (TestPickEvent) -> Unit,
    onTopicsSelected: (TestPickEvent) -> Unit, //Drop down or just scrollable list view ?
    onPickUnanswered: (TestPickEvent) -> Unit,
    onPickWrongAnswered: (TestPickEvent) -> Unit,
    onPickTime: (TestPickEvent) -> Unit,
    onTestOptionState: (TestPickEvent) -> Unit,
    expanded: Boolean ,
){
    val contextForToast = LocalContext.current.applicationContext
    var expanded1 by remember {
        mutableStateOf(expanded)
    }


    Column (
        modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {

        Button(
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
            onClick = { expanded1 = !expanded1 }) {
            Text(
                text = if (!expanded1) {
                    "Open options"
                } else {
                    "Close options"
                }
            )
        }


        Row() {
            Button(
                enabled = false,
                onClick = {
                Log.i(
                    "QuizOptionField",
                    "For your info: Switches need pop up with explanation."
                )
            }) {

                Text(text = "For now switches is disabled and test start with 3 questions")
            }
        }


        if (expanded1) {


            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Unanswered")
                Switch(
                    checked = unanswered.value,
                    onCheckedChange = { onPickUnanswered(TestPickEvent.PickUnanswered(unanswered.value)) })
            }
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Wrongly answered")
                Switch(
                    checked = wronglyAnswered.value,
                    onCheckedChange = {
                        onPickWrongAnswered(
                            TestPickEvent.PickWrongAnswered(
                                wronglyAnswered.value
                            )
                        )
                    })
            }
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Time based / not yet")
                Switch(
                    checked = answerTime.value,
                    onCheckedChange = { onPickTime(TestPickEvent.PickTime(answerTime.value)) })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    //            .height(56.dp)
                    .toggleable(
                        value = selectedTopics == allTopicList,
                        onValueChange = {
                            onCheckAllTopics(TestPickEvent.CheckAllTopics(checkAllTopics.value))

                        },
                        role = androidx.compose.ui.semantics.Role.Checkbox
                    )
                    .background(MaterialTheme.colors.primaryVariant),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val listOfIds: MutableList<Int> = mutableListOf()
                allTopicList.forEach { listOfIds.add(it.topicId) }

                Text(
                    text = "Select all topics",
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = selectedTopics == listOfIds,
                    onCheckedChange = null // null recommended for accessibility with screenreaders
                )
            }

            LazyColumn() {
                item {
                    if (allTopicList.isNotEmpty()) {
                        val checkBoxRow = allTopicList.forEachIndexed() { index, topic ->
                            val onClickHandle = {
                                onTopicsSelected(TestPickEvent.CheckTopics(index))
                            }

                            CheckBoxRow1(
                                modifier = Modifier,
                                checkedState = selectedTopics.contains(index),
                                onStateChange = { onClickHandle.invoke() },
                                checkBoxText = topic.topic
                            )

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckBoxRow1(
    modifier: Modifier = Modifier,
    checkedState: Boolean,
    onStateChange: () ->Unit,
    checkBoxText: String

    ){
    Surface(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                //            .height(56.dp)
                .toggleable(
                    value = checkedState,
                    onValueChange = {
                        onStateChange()

                    },
                    role = androidx.compose.ui.semantics.Role.Checkbox
                )
                .background(MaterialTheme.colors.primaryVariant)
            ,

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = checkBoxText,
                style = MaterialTheme.typography.body1,
//                modifier = Modifier.padding(start = 16.dp)
            )
            Checkbox(
                checked = checkedState,
                onCheckedChange = null // null recommended for accessibility with screenreaders
            )
        }

    }
}

@Preview
@Composable
fun CheckBoxRowPreview() {
    CheckBoxRow1(checkedState = false, onStateChange = {  }, checkBoxText = "Some text of desc.")
}

@Preview
//@Preview("TestPick - QuizOptionsFieldPreview2" , uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QuizOptionsFieldPreview2(modifier: Modifier = Modifier) {
    val topicList : List<Topic> = listOf(Topic(1,"sa"), Topic(2,"Second topic"), Topic(3,"NewTopic"))
    QuizOptionsField2(
        expanded = true,
        onTopicsSelected = { /*TODO*/ },
        onTestOptionState = {},
        onPickTime = {},
        modifier = Modifier,
        onPickUnanswered = {},
        onPickWrongAnswered = {},
        questionCount = 8,
        allTopicList = topicList,
        wronglyAnswered = TestPickEvent.PickWrongAnswered(false),
        answerTime = TestPickEvent.PickTime(true),
        unanswered = TestPickEvent.PickUnanswered(true),
        checkAllTopics = TestPickEvent.CheckAllTopics(true),
        onCheckAllTopics = {},
        selectedTopics = listOf(1,2,3)
    )
}