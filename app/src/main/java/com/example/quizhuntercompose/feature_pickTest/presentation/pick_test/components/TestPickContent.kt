package com.example.quizhuntercompose.feature_pickTest.presentation

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.cor.util.TestTags
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickEvent
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickOptionsState
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.components.QuizSelectOptionsTopics
import kotlin.math.roundToInt


@Composable
fun TestPickContent(
    //    navController: NavController,
    viewModel: TestPickViewModel,
    testPickOptionsState: TestPickOptionsState,
    onNavigationTest: () -> Unit,
) {
//    val verticalScroll = rememberScrollState()
//    var verticalBias by remember { mutableStateOf(-1f) }
//    val  alignment by animateHorizontalAlignmentAsState(verticalBias)

//    val fabVisibility by derivedStateOf {
//        listState.firstVisibleItemIndex == 0
//    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center, //TODO End - when  scroll & collapse. Use implement Material 3
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(
                    text =  stringResource(id = R.string.startQuiz),
                    color = MaterialTheme.colors.surface )
                },
                onClick = { onNavigationTest.invoke() }, //Entry Point Activity -> onNavigationRequested
                shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
                backgroundColor = MaterialTheme.colors.secondary,
                icon = { Icon (Icons.Filled.Build, contentDescription = null )} )
        },
        isFloatingActionButtonDocked = false //Bottom navigation

    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // We keep track if the options box(column) is expanded or not in this variable
            var isExpanded by remember { mutableStateOf(false) }

            // We toggle the isExpanded variable when we click on this Column
            QuizSelectOptionsTopics(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .weight(0.5f, false),
//                    .verticalScroll(verticalScroll),
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
                selectedTopics = testPickOptionsState.pickedTopicId,
            )

            Spacer(modifier = Modifier.height(16.dp))

            TestPickContentSliderDetails(
                modifier = Modifier.weight(0.2f,false),
                steps = viewModel.uiState.value.totalCount,
                currentSteps = viewModel.uiState.value.count,
                onSlide = viewModel::onEvent
            )
        }
    }


}

@Composable
private fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment.Horizontal> {
    val bias by animateFloatAsState(targetBiasValue)
    return remember { derivedStateOf { BiasAlignment.Horizontal(bias) }
    }
}

@Composable
fun TestPickContentSliderDetails(modifier: Modifier, steps: Int, currentSteps: Int, onSlide: (TestPickEvent) -> Unit){
    Column(modifier = modifier.fillMaxWidth(1f)) {

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
    var sliderPosition by remember { mutableStateOf(currentSteps) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = currentSteps.toString(),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.testTag(TestTags.PICK_QUIZ_TEXT_VIEW_QUESTION_COUNT))

        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = { sliderPosition = it.roundToInt() },
            valueRange = 0f..maxSteps.toFloat(),
            onValueChangeFinished = {
                Log.i("TestPickCont", "onValueChange in slide happened + sliderPos: $sliderPosition")
                onSlide(TestPickEvent.ChooseCount(value = sliderPosition ))
            },
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.secondary,
                activeTrackColor = MaterialTheme.colors.surface,

                ),
            modifier = Modifier.testTag(TestTags.PICK_QUIZ_SLIDER)
        )
    }

    //return intCount?
}

@Composable
@Preview
fun TestPickContentPreview(){
    val topicList : List<Topic> = listOf(Topic(0,"First or zero topic"), Topic(1,"Some random"), Topic(2,"Second topic"))

    val slideLocation: Int =4

    TestPickContentSliderDetails( steps = 5, currentSteps = slideLocation, onSlide = {  }, modifier = Modifier )
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
    MaterialTheme() {

        StepsSliderSample(1,4, onSlide = {}, currentSteps = 3 )
    }
}

@Composable
@Preview
fun TestPickContent2Preview(){
    TestPickContent(viewModel = hiltViewModel(), testPickOptionsState = TestPickOptionsState(questions = emptyList(), topics = emptyList(), isOptionsSectionVisible = false, count = 5, totalCount = 6, unanswered = false, wrongAnswersState =  false, answerTime = true, pickedTopicId = emptyList()), onNavigationTest =  {})
}