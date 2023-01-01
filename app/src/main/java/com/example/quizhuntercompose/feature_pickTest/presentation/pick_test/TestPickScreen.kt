package com.example.quizhuntercompose.feature_pickTest.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickOptionsState

@Composable
fun TestPickScreen(){
    val _quizPickOption = TestPickOptionsState()

    Column(
        modifier= Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
    ) {
        //TODO Top app bar
        TestPickContent(testPickOptionsState = _quizPickOption)

    }
}

@Composable
@Preview
fun TestPickScreenPreview(){
    TestPickScreen()
}