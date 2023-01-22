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
//    quizUseCase: @JvmSuppressWildcards QuizUseCase,

    viewModel: TestPickViewModel,
    testPickOptionsState: TestPickOptionsState,
    topicList: List<Topic>,
    onNavigationTest: () -> Unit,
) {
//    viewModel = hiltViewModel()
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

        StepsSliderSample( steps = testPickOptionsState.pickedQuestions.size/2, maxSteps = testPickOptionsState.pickedQuestions.size, onSlide = viewModel::onEvent )
        //{
//            testPickOptionsState.count = it
        //}
        Spacer(modifier = Modifier.height(16.dp))

        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }

        // We toggle the isExpanded variable when we click on this Column
        QuizOptionsField2(
            modifier = Modifier.clickable { isExpanded = !isExpanded },
//            pickTestViewModel = viewModel,
//            uiState = viewModel.uiState.value,

            unanswered = TestPickEvent.PickUnanswered(viewModel.uiState.value.unanswered),
            answerTime = TestPickEvent.PickTime(viewModel.uiState.value.answerTime),
            wronglyAnswered = TestPickEvent.PickWrongAnswered(viewModel.uiState.value.wrongAnswersState),

            onTopicsSelected = viewModel::onEvent,
            expanded = false,
            onTestOptionState = viewModel::onEvent,
            onPickWrongAnswered = viewModel::onEvent,
            onPickUnanswered = viewModel::onEvent,
            onPickTime = viewModel::onEvent,
            checkedTopics = viewModel::onEvent,
            allTopicQuestions =viewModel.topicNames,
//            onCheckTopicQuestions =

        )

    }


}

@Composable
fun StepsSliderSample(steps: Int,
                      maxSteps: Int,
                      onSlide: (TestPickEvent) -> Unit )   {
    var sliderPosition by remember { mutableStateOf(0f) }
    Text(text = sliderPosition.toInt().toString(), style = MaterialTheme.typography.h3)
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
    TestPickContent(
        viewModel = hiltViewModel(),
        TestPickOptionsState(count = 5, pickedTopic = listOf("1","2"), pickedQuestions = emptyList(), pickedAllTopic = true,  questions = emptyList(), topics = emptyList(), isOptionsSectionVisible = true, answerTime = false, unanswered = false, wrongAnswersState = false ),
        onNavigationTest = {},
        topicList = emptyList()
    )
}

@Composable
@Preview
fun StepsSliderSamplePreview(){
    StepsSliderSample(5,9, onSlide = {} )
}