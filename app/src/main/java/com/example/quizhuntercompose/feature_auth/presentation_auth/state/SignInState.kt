package com.example.quizhuntercompose.feature_auth.presentation_auth.state


data class SignInState(
    val isLoading: Boolean = false,
    val signIn: Any? = null,
    val error: String = ""
)