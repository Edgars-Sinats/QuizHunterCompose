package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizhuntercompose.cor.util.TestTags
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickEvent

// TODO  Interactive + Bounce layout
@Composable
fun QuizSelectOptionsTopics(
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
    expanded: Boolean,
    onUploadTestToFirebase: (TestPickEvent) -> Unit,
    onDownloadTestFromFirebase: (TestPickEvent) -> Unit
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

        Row(modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp)) {

            OutlinedButton(
                onClick = { onUploadTestToFirebase.invoke(TestPickEvent.UploadTestQuestions) }
            ) {
                modifier.weight(1f)
                Text(text = "Upload Test")
            }

            OutlinedButton(
                onClick = { onDownloadTestFromFirebase.invoke(TestPickEvent.DownloadTestQuestions) }
            ) {
                modifier.weight(1f)
                Text(text = "Download test")
            }
        }

        Button(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .testTag(TestTags.PICK_QUIZ_OPTIONS_ENABLER_BUTTON),
            onClick = { expanded1 = !expanded1 } ) {
            Text(
                text = if (!expanded1) {
                    "Open options"
                } else {
                    "Close options"
                }
            )
        }

        if (expanded1) {

            //Options of Switches
            Column() {
                Row(
                    modifier
                        .fillMaxWidth()
                        .testTag(TestTags.PICK_QUIZ_OPTIONS_UNANSWERED_ROW),
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Check All Topics row
            Row(
                modifier = modifier

                    .fillMaxWidth()
                    //.padding(all = 4.dp)
                    //.height(70.dp)
                    .toggleable(
                        value = selectedTopics == allTopicList,
                        onValueChange = {
                            onCheckAllTopics(TestPickEvent.CheckAllTopics(checkAllTopics.value))

                        },
                        role = androidx.compose.ui.semantics.Role.Checkbox
                    )
//                    .weight(0.2f, false)
                    .background(MaterialTheme.colors.primaryVariant),
//                verticalAlignment = Alignment.CenterVertically,

                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val listOfIds: MutableList<Int> = mutableListOf()
                //Removed -1
                allTopicList.forEach { listOfIds.add(it.topicId ) }

                Text(
                    text = "Select all topics",
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center )
                Checkbox(
                    checked = selectedTopics == listOfIds,
                    onCheckedChange =  { onCheckAllTopics(TestPickEvent.CheckAllTopics(checkAllTopics.value)) } // null recommended for accessibility with screenreaders
                )
            }

            LazyColumn(
//                modifier
//                    .weight(0.8f, false)
//                    .verticalScroll(
////                        flingBehavior = stat,
//                        state = rememberScrollState() )
            )
            {
                item {
                    if (allTopicList.isNotEmpty()) {
                        val checkBoxRow = allTopicList.forEachIndexed() { index, topic ->
                            val onClickHandle = {
                                onTopicsSelected(TestPickEvent.CheckTopics(topic.topicId))
                            }

                            CheckBoxRow1(
                                modifier = Modifier,
                                checkedState = selectedTopics.contains(topic.topicId),
                                onStateChange = { onClickHandle.invoke() },
                                checkBoxText = topic.topic
                            )

                        }
                    } else {
                        //Todo add default row - Sorry, what! No items :o ?
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
                .padding(all = 2.dp)
                .fillMaxWidth()
                //            .height(56.dp)
                .toggleable(
                    value = checkedState,
                    onValueChange = {
                        onStateChange()

                    },
                    role = androidx.compose.ui.semantics.Role.Checkbox
                )
                .background(MaterialTheme.colors.surface)

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

//@Preview
////@Preview("TestPick - QuizOptionsFieldPreview2" , uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun QuizOptionsFieldPreview2(modifier: Modifier = Modifier) {
//    val topicList : List<Topic> = listOf(Topic(0,"sa"), Topic(1,"Second topic"), Topic(2,"NewTopic"))
//    QuizSelectOptionsTopics(
//        expanded = true,
//        onTopicsSelected = { /*TODO*/ },
//        onTestOptionState = {},
//        onPickTime = {},
//        modifier = Modifier,
//        onPickUnanswered = {},
//        onPickWrongAnswered = {},
//        questionCount = 8,
//        allTopicList = topicList,
//        wronglyAnswered = TestPickEvent.PickWrongAnswered(false),
//        answerTime = TestPickEvent.PickTime(true),
//        unanswered = TestPickEvent.PickUnanswered(true),
//        checkAllTopics = TestPickEvent.CheckAllTopics(true),
//        onCheckAllTopics = {},
//        selectedTopics = listOf(1,2,3)
//    )
//}