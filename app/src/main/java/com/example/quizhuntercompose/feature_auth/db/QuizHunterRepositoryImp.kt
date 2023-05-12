package com.example.quizhuntercompose.feature_auth.db

import com.example.quizhuntercompose.core_dbo.test.TestDao
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.core_dbo.toDomain
import com.example.quizhuntercompose.core_dbo.toEntity
import com.example.quizhuntercompose.domain.model.QuizHunterUser
import com.example.quizhuntercompose.core_dbo.user.UserDao
import com.example.quizhuntercompose.core_dbo.user.toQuizHunterUser
import com.example.quizhuntercompose.core_dbo.user.toUserEntity
import com.example.quizhuntercompose.domain.model.Test
import com.example.quizhuntercompose.feature_auth.domain.QuizHunterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizHunterRepositoryImp @Inject constructor(
    private val userDao: UserDao,
    private val testDao: TestDao

) :QuizHunterRepository{
    override fun getQuizHunterUser(): Flow<QuizHunterUser?> {
        return userDao.getUser().map { it?.toQuizHunterUser() }
    }

    override suspend fun saveQuizHunterUser(user: QuizHunterUser) {
        return userDao.insertUser(user.toUserEntity())
    }

    override suspend fun removeQuizHunterUserRecord() {
        return userDao.deleteUser()
    }

    override fun isUserPremiumLocal(): Flow<Boolean> {
        return flow {
            userDao.getUser().collect() { user ->
                user?.let { emit( it.toQuizHunterUser().isUserPremium() ) }
            }
        }
    }

    override fun isUserAdmin(testId: Int): Flow<Boolean> {
        return flow {
            userDao.getUser().collect() { user ->
                user?.let { emit( it.toQuizHunterUser().isUserAdmin(testId) ) }
            }
        }
    }

    override fun isUserTeacher(testId: Int): Flow<Boolean> {
        return flow {
            userDao.getUser().collect() { user ->
                user?.let { emit( it.toQuizHunterUser().isUserTeacher(testId) ) }
            }
        }
    }

    override fun getTests(): Flow<List<Test>> {
        return testDao.getTests().map { it.toDomain() } //.toDomain
    }

    override fun getTestsEntity(): Flow<List<TestEntity>> {
        return testDao.getTests().map { it } //.toDomain
    }

    override suspend fun saveTests(tests: List<Test>) {
        return testDao.insertTests(tests = tests.toEntity())  //.toDomain
    }

    override suspend fun saveTest(test: TestEntity) {
        return testDao.insertTest( test )
    }

    override suspend fun addFavoriteTest(testId: Int, isFavorite: Boolean) {
        return testDao.updateIsFavorite(testId = testId, isFavorite = isFavorite)
    }

    override suspend fun removeAllTests() {
        testDao.removeAllTests()
    }
}