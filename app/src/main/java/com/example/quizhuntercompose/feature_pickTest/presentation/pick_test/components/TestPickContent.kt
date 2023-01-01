package com.example.quizhuntercompose.feature_pickTest.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.RawQuery
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickEvent
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickOptionsState
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel


@Composable
fun TestPickContent(
    //    navController: NavController,
    viewModel: TestPickViewModel = hiltViewModel(),
    testPickOptionsState: TestPickOptionsState
) {
//    viewModel = hiltViewModel()
    TextButton(
        onClick = {

        viewModel.onEvent(TestPickEvent.StartQuiz)
                  },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Start Quiz")
    }

    StepsSliderSample(steps = 2, maxSteps = testPickOptionsState.questions.count()){
        testPickOptionsState.count = it
    }
    Spacer(modifier = Modifier.height(16.dp))

    // We keep track if the message is expanded or not in this
    // variable
    var isExpanded by remember { mutableStateOf(false) }

    // We toggle the isExpanded variable when we click on this Column
    QuizOptionsField2(

        modifier = Modifier.clickable { isExpanded = !isExpanded },
        testOptionState = testPickOptionsState,

        onTopicsSelected = { /*TODO*/ },
        expanded = false,

    )

}

@Composable
fun StepsSliderSample(steps: Int, maxSteps: Int, onSlide: (count: Int) -> Unit = {} )   {
    var sliderPosition by remember { mutableStateOf(0f) }
    Text(text = sliderPosition.toInt().toString(), style = MaterialTheme.typography.h3)
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = 0f..maxSteps.toFloat(),
        onValueChangeFinished = {
            onSlide.invoke(sliderPosition.toInt())
        },
        steps = steps,
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colors.secondary,
            activeTrackColor = MaterialTheme.colors.secondary
        )
    )
    //return intCount?
}