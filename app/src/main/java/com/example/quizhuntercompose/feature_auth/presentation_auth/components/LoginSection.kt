package com.example.quizhuntercompose.feature_auth.presentation_auth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizhuntercompose.R

@Composable
fun LoginSection(
    customLoginButton: () -> Unit,
    googleSignInButton: () -> Unit,
    isEnabled: Boolean
) {

    AnimatedVisibility(visible = isEnabled) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomLoginButton(
                text = stringResource(id = R.string.LOGIN),
                modifier = Modifier.fillMaxWidth(),
            ) {
                customLoginButton()
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.____OR____),
                modifier = Modifier.padding(6.dp),
                fontSize = 16.sp,
                color = MaterialTheme.colors.error.copy(0.1f),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

//            GoogleSignInButton(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                googleSignInButton()
//            }
        }
    }
}