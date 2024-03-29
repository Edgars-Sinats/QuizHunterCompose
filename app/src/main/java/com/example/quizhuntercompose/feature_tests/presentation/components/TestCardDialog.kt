package com.example.quizhuntercompose.feature_tests.presentation.components

import android.content.res.Configuration
import android.graphics.fonts.SystemFonts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.feature_pickTest.presentation.pick_test.TestPickEvent
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TestCardDialog(
    onDismiss: () -> Unit,
    onOpenTest: () -> Unit,
    onStared: () -> Unit,
    test: TestEntity,
    modifier: Modifier,
    onDownloadTestFromFirebase: () -> Unit,
    onUploadTestToFirebase: () -> Unit
){

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 8.dp
        ) {
            Column() {
                Row() {
                    Column(modifier
                        .weight(1f)
                        .padding(start = 32.dp, end = 32.dp)) {

                        OutlinedButton(
                            onClick = { onUploadTestToFirebase.invoke() }
                        ) {
                            modifier.weight(1f)
                            Text(text = "Upload Test")
                        }

                        OutlinedButton(
                            onClick = { onDownloadTestFromFirebase.invoke() }
                        ) {
                            modifier.weight(1f)
                            Text(text = "Download test")
                        }
                    }
//                    Spacer(Modifier.weight(1f))
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

                Column(modifier = modifier) {
//                    Row(Modifier.padding(vertical = 4.dp)) {
//                        Text(text = "Test created at: " + Date(test.testCreated) )
////                        Spacer(modifier = Modifier.weight(1f))
////                        Text(text = "Test last updated: " + test.testModified.toString())
//                    }
                    Row(Modifier.padding(vertical = 4.dp)) {
                        val sdf = SimpleDateFormat("dd/MM//yyyy")
                        val netDate = Date(test.testCreated)
                        val formattedD = sdf.format(netDate)

                        Text(text = "Test modified at: $formattedD")
//                        Spacer(modifier = Modifier.weight(1f))
//                        Text(text = "Test last updated: " + test.testModified.toString())
                    }

                    Text(text = test.testDescription?: "")

                    Row(modifier = modifier.padding(4.dp)) {
                        TextButton(onClick = onDismiss ) {
                            Text(text = "Close preview")
                        }
                        //TODO BROWSE QUESTIONS
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = onOpenTest) {
                            Text(text = "Open test")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
@Preview("DarkScreen" , uiMode = Configuration.UI_MODE_NIGHT_YES)
fun TestCardDialogPreview(){

    val testItem = TestEntity(
        testId = 1,
        isFavorite = true,
        testImageUrl = "www.someCoolImage.firebase.server.com",
        testModified = 0,
        testCreated = 0,
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
        test = testItem,
        modifier = Modifier,
        onUploadTestToFirebase = {},
        onDownloadTestFromFirebase = {}
    )
}