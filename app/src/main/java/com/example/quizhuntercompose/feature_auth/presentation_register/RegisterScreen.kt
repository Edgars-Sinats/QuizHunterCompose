package com.example.quizhuntercompose.feature_auth.presentation_register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.components.BackStackButton
import com.example.quizhuntercompose.components.QuizHunterTextField
import com.example.quizhuntercompose.cor.util.AppConstants
import com.example.quizhuntercompose.cor.util.isValidEmail
import com.example.quizhuntercompose.cor.util.isValidPassword
import com.example.quizhuntercompose.domain.model.QuizHunterUser
import com.example.quizhuntercompose.feature_auth.presentation_auth.components.CustomClickableText
import com.example.quizhuntercompose.feature_auth.presentation_auth.components.CustomLoginButton
import com.talhafaki.composablesweettoast.util.SweetToastUtil

@Composable
fun RegisterScreen(
    navigateToSignInScreen: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
){
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    val signUpState = viewModel.signUp.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .requiredHeight(40.dp)
            ) {
                BackStackButton {
                    popBackStack()
                }
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = AppConstants.CREATE_ACCOUNT,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ) {}
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomClickableText(
                    text = AppConstants.SIGN_UP_TO_GET_STARTED,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ) {}
            }

            Spacer(modifier = Modifier.height(60.dp))

            QuizHunterTextField(
                text = userName,
                placeholder = "Username",
                isPasswordTextField = false,
                onValueChange = { userName = it.trim() },
                isError = isError,
                errorMsg = "*Enter valid username",
                trailingIcon = {
                    if (email.isNotBlank()) {
                        IconButton(onClick = { email = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            QuizHunterTextField(
                text = email,
                placeholder = "Email",
                isPasswordTextField = false,
                onValueChange = { email = it.trim() },
                isError = isError,
                errorMsg = "*Enter valid email address",
                trailingIcon = {
                    if (email.isNotBlank()) {
                        IconButton(onClick = { email = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            QuizHunterTextField(
                text = password,
                placeholder = "Password",
                isPasswordTextField = !isPasswordVisible,
                onValueChange = { password = it },
                isError = isError,
                errorMsg = "*Enter valid password",
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Person
                                            else Icons.Default.Clear,
                            tint = Color.Gray,
                            contentDescription = "Password Toggle"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )

            )

            Spacer(modifier = Modifier.weight(0.2f))

            CustomLoginButton(
                text = "REGISTER",
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isValidEmail(email) && isValidPassword(password)) {
                    val quizHunterUser = QuizHunterUser(
                        userName = userName,
                        email = email
                    )
                    viewModel.signUp(quizHunterUser, password)
                } else {
                    isError = !isValidEmail(email) || !isValidPassword(password)
                }
            }

            Spacer(modifier = Modifier.weight(0.4f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                CustomClickableText(
                    text = "already have an account? ",
                    fontSize = 16.sp
                ) {}
                Spacer(modifier = Modifier.width(4.dp))
                CustomClickableText(
                    text = "Login",
                    color = Color.Magenta,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W500
                ) {
                    popBackStack()
                    navigateToSignInScreen()
                }
            }
        }
    }

    if (signUpState.value.isLoading) {
        LaunchedEffect(Unit) {
            isEnabled = !isEnabled
            isLoading = !isLoading
        }
    }

    if (signUpState.value.signUp != null) {
        SweetToastUtil.SweetSuccess(
            message = "Account created successfully",
            padding = PaddingValues(bottom = 24.dp)
        )
        LaunchedEffect(Unit) {
            val quizHunterUser = QuizHunterUser(
                userName = userName ,
                email = email,
                userUid = signUpState.value.signUp?.user?.uid
            )
            viewModel.addUserCredential(quizHunterUser)
            popBackStack()
            navigateToSignInScreen()
        }
    }

    if (signUpState.value.error.isNotBlank()) {
        LaunchedEffect(Unit) {
            isEnabled = !isEnabled
            isLoading = !isLoading
        }

        val errorMsg = signUpState.value.error
        SweetToastUtil.SweetError(
            message = errorMsg,
            padding = PaddingValues(bottom = 24.dp)
        )
    }

}