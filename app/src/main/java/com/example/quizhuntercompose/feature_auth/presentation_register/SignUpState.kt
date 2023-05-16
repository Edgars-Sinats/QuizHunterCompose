package com.example.quizhuntercompose.feature_auth.presentation_register

import com.google.firebase.auth.AuthResult

data class SignUpState(
    val isLoading: Boolean = false,
    val signUp: AuthResult? = null,
    val error: String = ""
)
