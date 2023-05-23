package com.example.quizhuntercompose.feature_tests.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
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
    modifier: Modifier = Modifier.background(color = MaterialTheme.colors.surface.copy(alpha = 0.8f)),
    viewModel: TestsViewModel = hiltViewModel(),
//    navController: NavController,
    navigateToQuizPickScreen: (testId: String) ->Unit,
    language: String
) {
    val state = viewModel.state.collectAsState()
    val searchTest = remember { mutableStateOf(TextFieldValue("")) }
    val lazyListState = rememberLazyListState()
//    var isClicked by mutableStateOf(false)

    LaunchedEffect(true) {
        viewModel.userState()
        viewModel.getLanguage(language)
        viewModel.getTests()
//        viewModel.getUserCredential()
    }

    Scaffold(
        topBar = {
            TestsScreenTopBar(
                hint = "Search...",
                state = searchTest,
                deleteTests = { viewModel.deleteAllTests() },
                uploadTest = {
                    //TODO create edit&create test feature
//                    viewModel.uploadTests()
                }
            )

        },
        modifier = modifier
    ) { _ ->


        LazyVerticalGrid(
            modifier = modifier.padding(2.dp),
            //TODO check screen width, small screen => Grid cell 1
            columns = GridCells.Fixed(2),
            content = {
                Log.i(TAG, "All tests: ${state.value.tests}")
                items(state.value.tests){  it ->
                    TestGridItem(
                        testItem = it,
                        modifier = Modifier
                            .padding(2.dp)
                            .background(
                                if (it.isLocal) {
                                    MaterialTheme.colors.secondary
                                } else {
                                    MaterialTheme.colors.primary.copy(0.85f)
                                })
//                            .background(MaterialTheme.colors.background)
                        ,
                        onItemClick = {
                            if (viewModel.state.value.openedTest == null){
                                viewModel.openTestPreview(it)
                                Log.i(TAG, "opened testPreview: $it")
                            } else {
                                viewModel.closeTestPreview()
                            }
                        }
                    )

                    if (state.value.openedTest != null){
                        Log.i(TAG, "TestDialog opened..: $it")
                        TestCardDialog(
                            onDismiss = { viewModel.closeTestPreview() },
                            onOpenTest = {
                                Log.i(TAG, "opening test..: ${state.value.openedTest}")
                                viewModel.closeTestPreview()
                                navigateToQuizPickScreen.invoke(state.value.openedTest!!.testId.toString())
                                         },
                            onStared = { viewModel.starFavoriteTest(state.value.openedTest!!) },
                            test = state.value.openedTest!!,
                            modifier = modifier
                        )
                    }

                }
            }//LazyGridCont
        )

        ScrollButton(lazyListState = lazyListState)
    }

}

private const val TAG = "TestScreen"
