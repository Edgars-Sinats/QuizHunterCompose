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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.core_dbo.test.TestEntity

@Composable
fun TestGridItem(
    testItem: TestEntity,
    modifier: Modifier,
    onItemClick: (TestEntity) -> Unit
){
    Box(

        modifier = Modifier
//            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
//            .fillMaxSize()
//            .align(Alignment.CenterHorizontally)
            .clickable { onItemClick(testItem) }
            .background(MaterialTheme.colors.background),
        Center



    ) {
        Column(modifier.fillMaxWidth()) {

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                ) {

                //Test Image||Placeholder
                Spacer(modifier = modifier.weight(0.25f))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp)
                ){
                    if ((testItem.testImageUrl?.length ?: 0) < 2){
                        Image(painter = painterResource(id = R.drawable.profile_placeholder), contentDescription = "Empty test image")
                    }else {
                        AsyncImage(
                            model = testItem.testImageUrl ,
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                Spacer(modifier = modifier.weight(0.25f))
                //Test is Starer
                if (testItem.isFavorite){
                    Image(modifier = Modifier.padding(4.dp), painter = painterResource(id = R.drawable.ic_baseline_filled_star_24), contentDescription = "Stared")
                } else {
                    Image(modifier = Modifier.padding(4.dp), painter = painterResource(id = R.drawable.ic_baseline_empty_star_24), contentDescription = "Un-stared")
                }
            }

            //TestName
            Text(
                modifier = modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(1f),
                text = testItem.testName)

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
        , modifier = Modifier, onItemClick = {})
}