package com.example.quizhuntercompose.domain.model

import java.security.Key

data class QuizHunterUser(
    val userUid: String? = "",
    val userName: String? = "",
    val email: String? = "",
    val image: String? = "",
    val premium: Boolean = false,
    val teacher: Boolean = false,
    val admin: Boolean = false,
    val testKeys: Map<Int, String> = mapOf()
) {
    fun isUserPremium(): Boolean {
        return premium
    }

    fun isUserAdmin(): Boolean {
        return admin
    }

    fun hasTestKey(key: Int) : Boolean {
        return testKeys.containsKey(key) ?: false
    }
}
