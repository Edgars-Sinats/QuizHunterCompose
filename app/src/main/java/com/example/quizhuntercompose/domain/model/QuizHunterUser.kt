package com.example.quizhuntercompose.domain.model

import java.security.Key

data class QuizHunterUser(
    val userUid: String = "",
    val userName: String = "",
    val name: String? = "",
    val email: String? = "",
    val image: String? = "",
    val premium: Boolean? = false,
    val teacher: Boolean? = false,
    val admin: Boolean? = false,
    val testKeys: Map<String, String> = mapOf(),
    val languages: String? = ""
) {
    fun isUserPremium(): Boolean {
        return premium ?: false
    }

    fun isUserAdmin(testId: Int): Boolean {
        return admin ?: false
    }

    fun isUserTeacher(testId: Int): Boolean {
        return teacher ?: false
    }

    fun hasTestKey(testId: String) : Boolean {
        return testKeys.containsKey(testId) ?: false
    }

    fun getLanguages(languages: String?): List<String> {
        if (languages != null) {
            val str = languages
            val delimiter = ":"
            return str.split(delimiter).toList()
        }else{
            return emptyList()
        }
    }
}
