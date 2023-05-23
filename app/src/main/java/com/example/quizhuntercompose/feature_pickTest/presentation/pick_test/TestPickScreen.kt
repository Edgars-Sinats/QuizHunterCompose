package com.example.quizhuntercompose.feature_pickTest.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
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
    navigateToQuizScreen: (testId1: String) -> Unit,
    navigateToProfileScreen: () -> Unit,
    test_id: String
){
    LaunchedEffect(key1 = true){
        pickViewModel.getTestId(testId = test_id.toInt())
        pickViewModel.loadTopicsTests()
    }
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
                .background(color = MaterialTheme.colors.background)
                .padding(vertical = 16.dp)
                .testTag(tag = TestTags.TITLE_TEXT_FIELD)
        ) {

            //TODO Top app bar

            TestPickContent(testPickOptionsState = testPickOptionsState,   viewModel = pickViewModel, onNavigationTest = { navigateToQuizScreen(test_id) }) //topicList = topicList,

        }
    }
}

//@Composable
//@Preview
//fun TestPickScreenPreview(){
//    TestPickScreen(hiltViewModel(), onNavigationRequested = {})
//}