package com.example.quizhuntercompose.feature_auth.domain

import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.domain.model.QuizHunterUser
import com.example.quizhuntercompose.domain.model.Test
import kotlinx.coroutines.flow.Flow

interface QuizHunterRepository {

    //User
    fun getQuizHunterUser(): Flow<QuizHunterUser?>

    suspend fun saveQuizHunterUser(user: QuizHunterUser)

    suspend fun removeQuizHunterUserRecord()

    fun isUserPremiumLocal(): Flow<Boolean>

    fun isUserAdmin(testId: Int): Flow<Boolean>

    fun isUserTeacher(testId: Int): Flow<Boolean>

      //Test

    fun getTests(): Flow<List<Test>>

    fun getTestsEntity(): Flow<List<TestEntity>>

    fun getTestsLanguage(language: String): Flow<List<Test>>

    fun getTestsLanguageEntity(language: String): Flow<List<TestEntity>>

    suspend fun saveTests(tests: List<Test>)



    suspend fun saveTest(test: TestEntity)

    suspend fun addFavoriteTest(testId: Int, isFavorite: Boolean)

    //SignOut
    suspend fun removeAllTests()

}