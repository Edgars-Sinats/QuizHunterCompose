package com.example.quizhuntercompose.feature_auth.presentation_profile.components

//import androidx.compose.foundation.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Button
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
//import androidx.compose.ui.input.pointer.PointerIconDefaults.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.cor.util.AppConstants.QUIZ_PICK_SCREEN
//import com.example.quizhuntercompose.cor.util.AppConstants.SIGN_IN_WITH_GOOGLE


@Composable
fun QuizOptionsButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 48.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colors.primaryVariant
        )
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_baseline_sentiment_very_satisfied_24
            ),
            contentDescription = null
        )
        Text(
            text = QUIZ_PICK_SCREEN,
            modifier = Modifier.padding(6.dp),
            fontSize = 18.sp
        )
    }
}