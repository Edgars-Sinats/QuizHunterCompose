package com.example.quizhuntercompose.feature_auth.presentation_profile

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.cor.util.AppConstants.REVOKE_ACCESS_MESSAGE
import com.example.quizhuntercompose.cor.util.AppConstants.SIGN_OUT
import com.example.quizhuntercompose.cor.util.getAllTestLanguages
import com.example.quizhuntercompose.core_state.UserState
import com.example.quizhuntercompose.feature_auth.presentation_profile.components.*
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToAuthScreen: () -> Unit,
    navigateToQuizPickScreen: (language: String) -> Unit
){
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val userCredential = viewModel.userCredential.collectAsState()
    val isPremium by viewModel.isUserPremium().collectAsState(initial = false)
    val isAuthedUser = viewModel.authState.value != (UserState.UnauthedUser)
    val isAuthedUser1 = viewModel.authState.value !is UserState.UnauthedUser
    val updateProfilePictureState = viewModel.updateProfilePictureState.collectAsState()

    val selectedLanguage = viewModel.selectedLanguage.value

    Log.i("TAG", "UserState: ${viewModel.authState.value} \n Should be equal to - UserState.UnAuthedUser: ${UserState.UnauthedUser}" )
    Log.i("TAG", "isAuthedUser: $isAuthedUser" )
    Log.i("TAG", "isAuthedUser1: $isAuthedUser1" )
    Log.i("TAG", "viewModel.authState: ${viewModel.authState}" )

    LaunchedEffect(true) {
        viewModel.updateUiState()
        viewModel.getUserCredential()
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
        bottomBar = {},
        content = { padding ->
            Log.i("ProfileScreen", "displayName: ${userCredential.value.userName.toString() }")
            Log.i("ProfileScreen","dsiplay name isNotEmpty(): ${{userCredential.value.userName.toString().isNotEmpty() }}")
            Log.i("ProfileScreen","dsiplay name .isBlank(): ${{userCredential.value.userName.toString().isBlank() }}")
            ProfileContent(
                padding = padding,
                photoUrl = userCredential.value.image.toString(),
                displayName = userCredential.value.userName.toString(),
                navigateToQuizPickScreen = { selectedLanguage?.let {
                    navigateToQuizPickScreen.invoke(it.value)
                    }
                },
                navigateToAuthScreen = navigateToAuthScreen,
                iconVisibility = isPremium,

                userImage = userCredential.value.image,
                isUserAuthed = isAuthedUser,
                updateProfilePictureState = updateProfilePictureState.value,
                clearUpdateProfilePictureState = { viewModel.clearUpdateProfilePictureState() },
                updateProfilePicture = { bitmap ->
                    viewModel.updateProfilePicture(bitmap = bitmap)
                },
                onSelectedLanguage  = { language-> viewModel.onSelectedLanguageChanged(language) },
                selectedLanguage = selectedLanguage

            )
//            val scrollState = rememberLazyListState()
//            LazyRow(
//                modifier = Modifier
//                    .padding(start = 8.dp, bottom = 8.dp),
//                state = scrollState,
//            ) {
//                items(getAllTestLanguages()){
//                    LanguageChipList(
//                        language = it.value,
//                        onSelectedCategoryChanged = viewModel::onSelectedLanguageChanged,
//                        isSelected = selectedLanguage == it,
//                        onExecuteSearch = {}
//                    )
//                }
//            }

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