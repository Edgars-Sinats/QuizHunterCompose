package com.example.quizhuntercompose.feature_tests.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.domain.model.Test

@Composable
fun TestCardDialog(
    onDismiss: () -> Unit,
    onOpenTest: () -> Unit,
    onStared: () -> Unit,
    test: TestEntity
){

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 8.dp
        ) {
            Column() {
                Row() {
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(
                            id = R.drawable.profile_placeholder
                        ),
                        contentDescription = "Logo off the quiz: " + test.testName )

                    Column(Modifier
                        .clickable(true, onClick = onStared)
                        .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(modifier =Modifier.padding(4.dp), text = if (test.isFavorite){"Is favorite"} else {"Set favorite"})
                        IconButton( onClick = onStared) {
                            Icon(
                                painter = painterResource(
                                id = if (test.isFavorite) {
                                    R.drawable.ic_baseline_filled_star_24 }
                                else {
                                    R.drawable.ic_baseline_empty_star_24 }
                                ),
                                contentDescription = "Test star is " + test.isFavorite,
                                Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,) {
//                    Spacer(modifier = Modifier.weight(0.2f))

                    Text(text = test.testName, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

                }//End of row - title

                Spacer(modifier = Modifier.padding(16.dp))

                Row(Modifier.padding(vertical = 4.dp)) {
                    Text(text = "Test created at: " + test.testCreated )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Test last updated: " + test.testModified)
                }

                test.testDescription?.let { Text(it) }

                Row(modifier = Modifier.padding(4.dp)) {
                    TextButton(onClick = onDismiss ) {
                        Text(text = "Close preview")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onOpenTest) {
                        Text(text = "Open test")
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun TestCardDialogPreview(){

    val testItem = TestEntity(
        testId = 1,
        isFavorite = true,
        testImageUrl = "www.someCoolImage.firebase.server.com",
        testModified = "",
        testCreated = "",
        testName = "Some cool test name with extra text,Some cool test name with extra text, Some cool test name with extra text,Some cool test name with extra text,Some cool test name with extra text",
        language = "Latvian",
        needKey = false,
        testRank = 2,
        isLocal = true,
        additionalInfo = null,
        testDescription = "Long long test description on repeat, Long long test description on repeat, Long long test description on repeat,Long long test description on repeat"
    )

    TestCardDialog(
        onOpenTest = {},
        onDismiss = {},
        onStared = {},
        test = testItem)
}
