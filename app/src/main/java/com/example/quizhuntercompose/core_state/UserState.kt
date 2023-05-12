package com.example.quizhuntercompose.core_state

sealed class UserState {
    object AuthedUser: UserState()
    object UnauthedUser: UserState()
    object PremiumUser: UserState()
}
