package com.example.quizhuntercompose.feature_auth.presentation_profile

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.cor.util.AppConstants.REVOKE_ACCESS_MESSAGE
import com.example.quizhuntercompose.cor.util.AppConstants.SIGN_OUT
import com.example.quizhuntercompose.feature_auth.presentation_profile.components.ProfileContent
import com.example.quizhuntercompose.feature_auth.presentation_profile.components.ProfileTopBar
import com.example.quizhuntercompose.feature_auth.presentation_profile.components.RevokeAccess
import com.example.quizhuntercompose.feature_auth.presentation_profile.components.SignOut
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToAuthScreen: () -> Unit,
    navigateToQuizPickScreen: () -> Unit
){
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        topBar = {
            ProfileTopBar(
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        },
        content = { padding ->
            ProfileContent(
                padding = padding,
                photoUrl = viewModel.photoUrl,
                displayName = viewModel.displayName,
                navigateToQuizPickScreen = navigateToQuizPickScreen

            )
        },
        scaffoldState = scaffoldState
    )

    SignOut(
        navigateToAuthScreen = { signedOut ->
            if (signedOut) {
                navigateToAuthScreen()
            }
        }
    )

    fun showSnackBar() = coroutineScope.launch {
        val result = scaffoldState.snackbarHostState.showSnackbar(
            message = REVOKE_ACCESS_MESSAGE,
            actionLabel = SIGN_OUT
        )
        if (result == SnackbarResult.ActionPerformed) {
            viewModel.signOut()
        }
    }

    RevokeAccess(
        navigateToAuthScreen = { accessRevoked ->
            if (accessRevoked) {
                navigateToAuthScreen()
            }
        },
        showSnackBar = {
            showSnackBar()
        }
    )
}