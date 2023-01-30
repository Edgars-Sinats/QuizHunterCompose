package com.example.quizhuntercompose.feature_pickTest.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickEvent
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickOptionsState
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import kotlin.math.roundToInt


@Composable
fun TestPickContent(
    //    navController: NavController,
    viewModel: TestPickViewModel,
    testPickOptionsState: TestPickOptionsState,
    onNavigationTest: () -> Unit,
) {
    Column() {
        TextButton(
            onClick = {
                onNavigationTest.invoke()
//                viewModel.onEvent(TestPickEvent.StartQuiz("Start"))

                      },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { },

        ) {
            Text(text = "Start Quiz", color = MaterialTheme.colors.secondary)
        }

        StepsSliderSample( steps = viewModel.uiState.value.totalCount, maxSteps = viewModel.uiState.value.totalCount, currentSteps = viewModel.uiState.value.count, onSlide = viewModel::onEvent )

        Spacer(modifier = Modifier.height(16.dp))

        // We keep track if the message is expanded or not in this variable
        var isExpanded by remember { mutableStateOf(false) }

        // We toggle the isExpanded variable when we click on this Column
        QuizOptionsField2(
            modifier = Modifier.clickable { isExpanded = !isExpanded },
            unanswered = TestPickEvent.PickUnanswered(viewModel.uiState.value.unanswered),
            answerTime = TestPickEvent.PickTime(viewModel.uiState.value.answerTime),
            wronglyAnswered = TestPickEvent.PickWrongAnswered(viewModel.uiState.value.wrongAnswersState),

            checkAllTopics = TestPickEvent.CheckAllTopics(viewModel.uiState.value.pickedAllTopic),
            onCheckAllTopics = viewModel::onEvent,
            questionCount = viewModel.uiState.value.totalCount,
            expanded = true,
            onTestOptionState = viewModel::onEvent,
            onPickWrongAnswered = viewModel::onEvent,
            onPickUnanswered = viewModel::onEvent,
            onPickTime = viewModel::onEvent,
            onTopicsSelected = viewModel::onEvent,
            allTopicList = viewModel.topicNames,
            selectedTopics = testPickOptionsState.pickedTopicId
        )

    }


}

@Composable
fun StepsSliderSample(steps: Int,
                      maxSteps: Int,
                      currentSteps: Int,
                      onSlide: (TestPickEvent) -> Unit )   {
    var sliderPosition by remember { mutableStateOf(0f) }
    Text(text = currentSteps.toString(), style = MaterialTheme.typography.h3)
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = 0f..maxSteps.toFloat(),
        onValueChangeFinished = {
            Log.i("TestPickCont", "onValueChange in slide happened + sliderPos: $sliderPosition")
            onSlide(TestPickEvent.ChooseCount(value = sliderPosition.roundToInt() ))
        },
        steps = steps,
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colors.secondary,
            activeTrackColor = MaterialTheme.colors.secondary
        )
    )
    //return intCount?
}

@Composable
@Preview
fun TestPickContentPreview(){
    val topicList : List<Topic> = listOf(Topic(0,"First or zero topic"), Topic(1,"Some random"), Topic(2,"Second topic"))

    TestPickContent(
        viewModel = hiltViewModel(   ),
        TestPickOptionsState(
            totalCount = 5,
            pickedTopicId = listOf(2,3),
            pickedQuestions = emptyList(),
            pickedAllTopic = true,
            questions = emptyList(),
            topics = topicList, //AllTopics
            isOptionsSectionVisible = true,
            answerTime = false,
            unanswered = false,
            wrongAnswersState = false ),
        onNavigationTest = {},
//        topicList = topicList
    )
}

@Composable
@Preview
fun StepsSliderSamplePreview(){
    StepsSliderSample(5,9, onSlide = {}, currentSteps = 3 )
}