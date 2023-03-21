package com.example.quizhuntercompose.feature_pickTest.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizhuntercompose.cor.util.TestTags
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickEvent
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickOptionsState
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.components.QuizOptionsField2
import kotlin.math.roundToInt


@Composable
fun TestPickContent(
    //    navController: NavController,
    viewModel: TestPickViewModel,
    testPickOptionsState: TestPickOptionsState,
    onNavigationTest: () -> Unit,
) {
    Column() {
        TestPickContentDetails(
            onNavigation = onNavigationTest,
            steps = viewModel.uiState.value.totalCount,
            currentSteps = viewModel.uiState.value.count,
            onSlide = viewModel::onEvent
        )
//        TextButton(
//            onClick = {
//                onNavigationTest.invoke()
////                viewModel.onEvent(TestPickEvent.StartQuiz("Start"))
//                      },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .clickable { },
//
//        ) {
//            Text(text = "Start Quiz", color = MaterialTheme.colors.secondary)
//        }
//
//        StepsSliderSample( steps = viewModel.uiState.value.totalCount, maxSteps = viewModel.uiState.value.totalCount, currentSteps = viewModel.uiState.value.count, onSlide = viewModel::onEvent )

        Spacer(modifier = Modifier.height(16.dp))

        // We keep track if the options box(column) is expanded or not in this variable
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
fun TestPickContentDetails(onNavigation: () -> Unit, steps: Int, currentSteps: Int, onSlide: (TestPickEvent) -> Unit){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(1f)) {
        TextButton(
            onClick = {
                onNavigation.invoke() //Entry Point Activity -> onNavigationRequested
//                viewModel.onEvent(TestPickEvent.StartQuiz("Start"))

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { },

            ) {
            Text(text = "Start Quiz", color = MaterialTheme.colors.onPrimary)
        }

        StepsSliderSample(
            steps = steps,
            maxSteps = steps,
            currentSteps = currentSteps,
            onSlide = onSlide
        )
    }
    }

@Composable
fun StepsSliderSample(steps: Int,
                      maxSteps: Int,
                      currentSteps: Int,
                      onSlide: (TestPickEvent) -> Unit )
{
    var sliderPosition by remember { mutableStateOf(0f) }

    Text(text = currentSteps.toString(), style = MaterialTheme.typography.h3, modifier = Modifier.testTag(TestTags.PICK_QUIZ_TEXT_VIEW_QUESTION_COUNT))

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
            activeTrackColor = MaterialTheme.colors.surface
        ),
        modifier = Modifier.testTag(TestTags.PICK_QUIZ_SLIDER)
    )
    //return intCount?
}

@Composable
@Preview
fun TestPickContentPreview(){
    val topicList : List<Topic> = listOf(Topic(0,"First or zero topic"), Topic(1,"Some random"), Topic(2,"Second topic"))

    TestPickContentDetails(onNavigation = {  }, steps = 5, currentSteps = 4, onSlide = { } )
//    TestPickContent(
//        viewModel = TestPickViewModel(),
//        TestPickOptionsState(
//            totalCount = 5,
//            pickedTopicId = listOf(2,3),
////            pickedQuestions = emptyList(),
//            pickedAllTopic = true,
//            questions = emptyList(),
//            topics = topicList, //AllTopics
//            isOptionsSectionVisible = true,
//            answerTime = false,
//            unanswered = false,
//            wrongAnswersState = false ),
//        onNavigationTest = {},
////        topicList = topicList
//    )
}

@Composable
@Preview
fun StepsSliderSamplePreview(){
    StepsSliderSample(5,9, onSlide = {}, currentSteps = 3 )
}