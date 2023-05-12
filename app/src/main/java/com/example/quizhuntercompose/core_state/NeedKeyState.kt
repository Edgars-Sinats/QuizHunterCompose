package com.example.quizhuntercompose.core_state

/**
    Tell whatever Test need key to access test or not.
 */
sealed class NeedKeyState {
    object Key : NeedKeyState()
    object NotKey : NeedKeyState()

    data class Messages(
        val keyMessage: String = "",
        val notKeyMessage: String = ""
    )
}
