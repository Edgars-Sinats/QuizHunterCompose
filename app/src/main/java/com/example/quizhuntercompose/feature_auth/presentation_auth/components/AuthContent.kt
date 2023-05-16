package com.example.quizhuntercompose.feature_auth.presentation_auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizhuntercompose.cor.util.AppConstants
import com.example.quizhuntercompose.cor.util.AppConstants.SIGN_IN_TO_ACCESS
import com.example.quizhuntercompose.cor.util.AppConstants.WELCOME_QUIZ_HUNTER
import com.example.quizhuntercompose.feature_auth.presentation_auth.state.SignInScreenState

@Composable
fun AuthContent(
    paddingValues: PaddingValues,
    oneTapSignIn: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    navigateToRegisterScreen: () -> Unit,
    popBackStack: () -> Unit,
    validateSigneIn: () -> Unit,
    screenState: SignInScreenState
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        contentAlignment = BottomCenter
    ) {
        SignInButton(
            onClick = oneTapSignIn
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(paddingValues)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = {
                    popBackStack()
                    navigateToHomeScreen()
                }
            ) {
                Icon(
                    tint = Color.Black,
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            CustomClickableText(
                text = WELCOME_QUIZ_HUNTER,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ) {}

            Spacer(modifier = Modifier.height(5.dp))

            CustomClickableText(
                text = SIGN_IN_TO_ACCESS,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            ) {}
        }

        Spacer(modifier = Modifier.height(60.dp))

//        DashCoinTextField(
//            text = screenState.email,
//            placeholder = "Email",
//            onValueChange = { viewModel.updateEmailState(it.trim()) },
//            isError = screenState.isError,
//            errorMsg = "*Enter valid email address",
//            isPasswordTextField = false,
//            singleLine = true,
//            trailingIcon = {
//                if (screenState.email.isNotBlank()) {
//                    IconButton(
//                        onClick = { viewModel.updateEmailState("") }
//                    ) {
//                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
//                    }
//                }
//            }
//        )

        Spacer(modifier = Modifier.height(16.dp))

//        DashCoinTextField(
//            text = screenState.password,
//            placeholder = "Password",
//            isPasswordTextField = !screenState.isPasswordVisible,
//            onValueChange = viewModel::updatePasswordState,
//            isError = screenState.isError,
//            singleLine = true,
//            errorMsg = "*Enter valid password",
//            trailingIcon = {
//                IconButton(
//                    onClick = { viewModel.updateIsPasswordVisible(!screenState.isPasswordVisible) }
//                ) {
//                    Icon(
//                        imageVector = if (screenState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                        tint = Color.Gray,
//                        contentDescription = "Password Toggle"
//                    )
//                }
//            },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Done
//            )
//
//        )


//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(end = 8.dp, top = 4.dp),
//            horizontalArrangement = Arrangement.End
//        )
//        {
//            CustomClickableText(
//                text = "Forgot Password?",
//                color = Gold,
//                fontSize = 14.sp,
//            ) {
//                navigateToForgotPassword()
//            }
//        }

        Spacer(modifier = Modifier.height(8.dp))

        LoginSection(
            customLoginButton = {
                validateSigneIn()
                                },
            googleSignInButton = {
                oneTapSignIn()
            },
            isEnabled = screenState.isVisible
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomClickableText(
                text = "New user? ",
                fontSize = 18.sp,
                color = Color.Black,
                ) {}
            Spacer(modifier = Modifier.width(4.dp))
            CustomClickableText(
                text = "Register",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500
            ) {
                navigateToRegisterScreen.invoke()
            }
        }
    }

    //        LoadingDots(isLoading = screenState.isLoading)

}