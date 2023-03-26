package com.example.quizhuntercompose.feature_pickTest.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.cor.util.TestTags
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickViewModel
import com.example.quizhuntercompose.ui.theme.Purple700

@Composable
fun TestPickScreen(
    pickViewModel: TestPickViewModel = hiltViewModel(),
    onNavigationRequested: () -> Unit
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

        Box(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) androidx.compose.ui.graphics.Color.Black else Purple700)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alpha = alphaAnim.value),
                imageVector = Icons.Default.Build,
                contentDescription = "Logo Icon",
                tint = androidx.compose.ui.graphics.Color.White
            )
        }
    } else {

        Column(
            modifier= Modifier
//            .verticalScroll(rememberScrollState()) // => Please try to remove the source of infinite constraints in the hierarchy above the scrolling container.
                .padding(vertical = 16.dp)
                .testTag(tag = TestTags.TITLE_TEXT_FIELD)
        ) {
            //TODO Top app bar
            TestPickContent(testPickOptionsState = testPickOptionsState,   viewModel = pickViewModel, onNavigationTest = onNavigationRequested) //topicList = topicList,

        }
    }
}

//@Composable
//@Preview
//fun TestPickScreenPreview(){
//    TestPickScreen(hiltViewModel(), onNavigationRequested = {})
//}