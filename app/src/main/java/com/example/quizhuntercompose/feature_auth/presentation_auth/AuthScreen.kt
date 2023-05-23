package com.example.quizhuntercompose.feature_auth.presentation_auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.domain.model.QuizHunterUser
import com.example.quizhuntercompose.feature_auth.presentation_auth.components.*
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit,
    navigateToRegisterScreen: () -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()
    val sigInState = viewModel.signIn.collectAsState()


    Scaffold (
        topBar = {
            AuthTopBar(viewModel.isAuthenticated.toString())
        },
        content = { padding ->

            AuthContent(
                paddingValues = padding,
                oneTapSignIn = {
                    viewModel.oneTapSignIn()
                },
                navigateToHomeScreen = {
                    navigateToProfileScreen()
                },
                navigateToRegisterScreen = {
                    navigateToRegisterScreen()
                },
                popBackStack = {
                },
                validateSigneIn = viewModel::validatedSignIn,
                screenState = screenState
            )
        }
    )

    if (sigInState.value.signIn != null) {
        SweetToastUtil.SweetSuccess(
            message = "You logged in successfully",
            padding = PaddingValues(bottom = 24.dp)
        )
        LaunchedEffect(Unit) {
            val quizUser = QuizHunterUser(
//                name =
            )
            delay(800)
            navigateToProfileScreen()
        }
    }

    val launcher =  rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credentials = viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credentials.googleIdToken
                val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                viewModel.signInWithGoogle(googleCredentials)
            } catch (it: ApiException) {
                print(it)
            }
        }
    }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    OneTapSignIn(
        launch = {
            launch(it)
        }
    )

    SignInWithGoogle (
        navigateToProfileScreen = { signedIn ->
            if (signedIn) {
                navigateToProfileScreen()
            }
        }
    )
}