package com.example.quizhuntercompose.feature_auth.presentation_profile

import android.util.Log
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.cor.util.AppConstants.REVOKE_ACCESS_MESSAGE
import com.example.quizhuntercompose.cor.util.AppConstants.SIGN_OUT
import com.example.quizhuntercompose.core_state.UserState
import com.example.quizhuntercompose.core_usecases.ProvideUserStateUseCase
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

    val userCredential = viewModel.userCredential.collectAsState()
    val isPremium by viewModel.isUserPremium().collectAsState(initial = false)
    val isAuthedUser = viewModel.authState.value != (UserState.UnauthedUser)
    val isAuthedUser1 = viewModel.authState.value !is UserState.UnauthedUser
    val updateProfilePictureState = viewModel.updateProfilePictureState.collectAsState()

    Log.i("TAG", "UserState: ${viewModel.authState.value} \n Should be equal to - UserState.UnAuthedUser: ${UserState.UnauthedUser}" )
    Log.i("TAG", "isAuthedUser: $isAuthedUser" )
    Log.i("TAG", "isAuthedUser1: $isAuthedUser1" )
    Log.i("TAG", "viewModel.authState: ${viewModel.authState}" )

    LaunchedEffect(true) {
        viewModel.updateUiState()
//        viewModel.getUserCredential()
    }

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
                photoUrl = userCredential.value.image.toString(),
                displayName = userCredential.value.userName.toString(),
                navigateToQuizPickScreen = navigateToQuizPickScreen,
                navigateToAuthScreen = navigateToAuthScreen,
                iconVisibility = isPremium,

                userImage = userCredential.value.image,
                isUserAuthed = isAuthedUser,
                updateProfilePictureState = updateProfilePictureState.value,
                clearUpdateProfilePictureState = { viewModel.clearUpdateProfilePictureState() },
                updateProfilePicture = { bitmap ->
                    viewModel.updateProfilePicture(bitmap = bitmap)
                }

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