package com.example.quizhuntercompose.feature_auth.presentation_auth.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.example.quizhuntercompose.cor.util.AppConstants.AUTH_SCREEN

@Composable
fun AuthTopBar(
    authState: String
) {
    TopAppBar (

        title = {
            Text(
                text = "$AUTH_SCREEN User is authed - $authState"
            )
        },
        backgroundColor = MaterialTheme.colors.background
    )
}