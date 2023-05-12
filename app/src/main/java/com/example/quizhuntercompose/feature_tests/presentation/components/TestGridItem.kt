package com.example.quizhuntercompose.feature_tests.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.domain.model.Test
import com.example.quizhuntercompose.feature_tests.presentation.TestsState

@Composable
fun TestGridItem(
    testItem: TestEntity,
    modifier: Modifier,
    onItemClick: (TestEntity) -> Unit
){
    Box(
        modifier = Modifier
            .background(
                if (testItem.isLocal) {
                    MaterialTheme.colors.secondary
                } else {
                    MaterialTheme.colors.primary
                })
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
//            .fillMaxSize()
            .clickable { onItemClick(testItem) }

    ) {
        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                ) {

                //Test Image
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(20.dp)
                ){
                    AsyncImage(
                        model = testItem.testImageUrl,
                        contentDescription = "Icon",
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                //Test is Star
                if (testItem.isFavorite){
                    Image(painter = painterResource(id = R.drawable.ic_baseline_filled_star_24), contentDescription = "Stared")
                } else {
                    Image(painter = painterResource(id = R.drawable.ic_baseline_empty_star_24), contentDescription = "Un-stared")
                }

            }



            //TestName
            Text(text = testItem.testName)

        }
    }
}

@Composable
@Preview
fun TestGridItemPreview(){
    TestGridItem(
        testItem = TestEntity(
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
        , modifier = Modifier, onItemClick = {})
}