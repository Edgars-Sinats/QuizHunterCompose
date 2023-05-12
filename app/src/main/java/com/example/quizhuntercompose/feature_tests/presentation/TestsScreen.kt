package com.example.quizhuntercompose.feature_tests.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizhuntercompose.feature_tests.presentation.components.ScrollButton
import com.example.quizhuntercompose.feature_tests.presentation.components.TestCardDialog
import com.example.quizhuntercompose.feature_tests.presentation.components.TestGridItem
import com.example.quizhuntercompose.navigation.Screen

enum class TestFilter {
    MY_TESTS, LATEST, LAST_UPDATE, RANK
}

@Composable
fun TestsScreen(
    viewModel: TestsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state.collectAsState()
    val searchTest = remember { mutableStateOf(TextFieldValue("")) }
    val lazyListState = rememberLazyListState()
//    var isClicked by mutableStateOf(false)

    Scaffold(
        topBar = {
            TestsScreenTopBar(
                hint = "Search...",
                state = searchTest,
                deleteTests = { viewModel.deleteAllTests() },
                uploadTest = { viewModel.uploadTests() }
            )

        }
    ) { paddingValues ->


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            content = {
                items(state.value.tests){  it ->
                    TestGridItem(
                        testItem = it,
                        modifier = Modifier.padding(8.dp),
                        onItemClick = {
                            if (viewModel.state.value.openedTest == null){
                                viewModel.openTestPreview(it.testId)
//                                isClicked = true
                            } else {
//                                isClicked = false
                                viewModel.closeTestPreview()
                            }
                        }
                        //TestsState .openedTests
                    )

                    if (state.value.openedTest != null){
                        TestCardDialog(
                            onDismiss = { viewModel.closeTestPreview() },
                            onOpenTest = { navController.navigate(Screen.QuizPickScreen.route + "/${it.testId}") },
                            onStared = { viewModel.starFavoriteTest(it.testId, !it.isFavorite ) },
                            test = it
                        )
                    }

                }
            },
            modifier = Modifier.padding(paddingValues)


        )


        ScrollButton(lazyListState = lazyListState)

    }

}