package com.example.quizhuntercompose.feature_pickTest.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel

@Composable
fun TestPickScreen(
    pickViewModel: TestPickViewModel = hiltViewModel(),
    onNavigationRequested: () -> Unit
){
    val testPickOptionsState = pickViewModel.uiState.value
//    val topicList = pickViewModel.topicNames


    Column(
        modifier= Modifier
//            .verticalScroll(rememberScrollState()) // => Please try to remove the source of infinite constraints in the hierarchy above the scrolling container.
            .padding(vertical = 16.dp)
    ) {
        //TODO Top app bar
        TestPickContent(testPickOptionsState = testPickOptionsState,   viewModel = pickViewModel, onNavigationTest = onNavigationRequested) //topicList = topicList,

    }
}

@Composable
@Preview
fun TestPickScreenPreview(){
//    TestPickScreen()
}