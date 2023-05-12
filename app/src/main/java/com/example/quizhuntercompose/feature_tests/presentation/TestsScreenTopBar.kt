package com.example.quizhuntercompose.feature_tests.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.quizhuntercompose.R

@Composable
fun TestsScreenTopBar(
    hint: String,
    modifier: Modifier = Modifier,
    state: MutableState<TextFieldValue>,
    deleteTests:() -> Unit,
    uploadTest:() -> Unit,
    ) {
    val focusRequester = remember { FocusRequester() }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = {deleteTests.invoke() }) {
            Text(text = "Delete all tests")
        }

        TextButton(onClick = {uploadTest.invoke() }) {
            Text(text = "Upload example tests")
        }
    }




    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_search_24),
            contentDescription = "Search",
            tint = Color.Red,
            modifier = Modifier
                .size(35.dp)
                .padding(start = 12.dp),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {


            BasicTextField(
                value = state.value,
                onValueChange = {
                    state.value = it
                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = Color.Cyan),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isHintDisplayed = !it.isFocused
                    }
                    .focusRequester(focusRequester),
                cursorBrush = SolidColor(value = Color.White)
            )

            if (isHintDisplayed) {
                Text(
                    text = hint,
                    color = Color.Black,
                    modifier = Modifier
                )
            }

        }

    }
}