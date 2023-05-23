package com.example.quizhuntercompose.feature_auth.presentation_profile.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.cor.util.TestLanguages
import com.example.quizhuntercompose.cor.util.getAllTestLanguages
import com.example.quizhuntercompose.feature_auth.presentation_profile.state.UpdatePictureState
import com.talhafaki.composablesweettoast.util.SweetToastUtil

@Composable
fun ProfileContent(
    padding: PaddingValues,
    photoUrl: String,
    displayName: String,
    navigateToQuizPickScreen:() -> Unit,
    navigateToAuthScreen: () -> Unit,
    iconVisibility: Boolean,
    userImage: String?,
    isUserAuthed: Boolean,
    updateProfilePictureState: UpdatePictureState,
    clearUpdateProfilePictureState: () -> Unit,
    updateProfilePicture: (bitmap: Bitmap) -> Unit,
    onSelectedLanguage: (language: String) -> Unit,
    selectedLanguage: TestLanguages?

    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(48.dp)
        )
        ProfilePictureBox(
            modifier = Modifier
                .padding(bottom = 10.dp),
            userImage = userImage,
            isUserAuthed = displayName.isNotEmpty(),
            updateProfilePictureState = updateProfilePictureState,
            clearUpdateProfilePictureState = clearUpdateProfilePictureState,
            updateProfilePicture = updateProfilePicture
        )
        if (displayName.isNotEmpty()) {
            Text(
                text = displayName,
                fontSize = 24.sp
            )
        } else {
            TextButton(onClick = navigateToAuthScreen) {
                Text(
                    text = stringResource(id = R.string.authenticate),
                    fontSize = 16.sp)
            }
        }
        Spacer(
            modifier = Modifier.height(48.dp)
        )
        QuizOptionsButton(
            onClick = navigateToQuizPickScreen
        )

        val scrollState = rememberLazyListState()
        LazyRow(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp),
            state = scrollState,
        ) {
            items(getAllTestLanguages()) {
                LanguageChipList(
                    language = it.value,
                    onSelectedCategoryChanged = onSelectedLanguage,
                    isSelected = selectedLanguage == it,
                    onExecuteSearch = {}
                )
            }

        }
    }
}

@Composable
fun ProfilePictureBox(
    modifier: Modifier = Modifier,
    userImage: String?,
    isUserAuthed: Boolean,
    updateProfilePictureState: UpdatePictureState,
    clearUpdateProfilePictureState: () -> Unit,
    updateProfilePicture: (bitmap: Bitmap) -> Unit,
) {

    /**
     * Show update profile picture state toasts
     */

    if (updateProfilePictureState.isFailure) {
        SweetToastUtil.SweetError(
            message = "Something goes wrong! Try again later",
            padding = PaddingValues(bottom = 24.dp)
        )
        clearUpdateProfilePictureState()
    }

    if (updateProfilePictureState.isSuccess) {
        SweetToastUtil.SweetSuccess(
            message = "Profile picture updated successfully",
            padding = PaddingValues(bottom = 24.dp)
        )
        clearUpdateProfilePictureState()
    }

    var isAsyncImageSate by remember {
        mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty)
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier
            .fillMaxWidth(fraction = 0.4f)
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = userImage,
            contentDescription = "Profile picture",
            onLoading = { isAsyncImageSate = it },
            onSuccess = { isAsyncImageSate = it },
            onError = { isAsyncImageSate = it },
            error = painterResource(id = R.drawable.profile_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )

        // Show progress indicator if -> picture is uploading || async image is loading
        if (
            updateProfilePictureState.isLoading
            || isAsyncImageSate is AsyncImagePainter.State.Loading
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colors.background.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onBackground
                )
            }
        }

        if (isUserAuthed) {
            PickImageButton(
                onPickImage = updateProfilePicture,
            )
        }
    }
}

@Composable
@Preview
fun ProfileContentPreview(){
    ProfileContent(padding = PaddingValues(),
        photoUrl = "",
        displayName = "",
        navigateToQuizPickScreen = { /*TODO*/ },
        navigateToAuthScreen = { /*TODO*/ },
        iconVisibility = true,
        userImage = "",
        isUserAuthed = true,
        updateProfilePictureState = UpdatePictureState(),
        clearUpdateProfilePictureState = { /*TODO*/ },
        updateProfilePicture = {},
        onSelectedLanguage = {},
        selectedLanguage = TestLanguages.LATVIAN)
}
