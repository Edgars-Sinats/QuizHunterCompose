package com.example.quizhuntercompose.feature_pickTest.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.components.ProgressBar
import com.example.quizhuntercompose.cor.util.TestTags
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel

@Composable
fun TestPickScreen(
    pickViewModel: TestPickViewModel,
    navigateToQuizScreen: () -> Unit
){
    val testPickOptionsState = pickViewModel.uiState.value
//    val topicList = pickViewModel.topicNames
    var startAnimation by remember { mutableStateOf(false) }
//    var startAn by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )
    if (pickViewModel.databaseLoading){

    ProgressBar()

    } else {

        Column(
            modifier= Modifier
//              .verticalScroll(rememberScrollState()) // => Please try to remove the source of infinite constraints in the hierarchy above the scrolling container.
                .padding(vertical = 16.dp)
                .testTag(tag = TestTags.TITLE_TEXT_FIELD)
        ) {

            //TODO Top app bar

            TestPickContent(testPickOptionsState = testPickOptionsState,   viewModel = pickViewModel, onNavigationTest = navigateToQuizScreen) //topicList = topicList,

        }
    }
}

//@Composable
//@Preview
//fun TestPickScreenPreview(){
//    TestPickScreen(hiltViewModel(), onNavigationRequested = {})
//}