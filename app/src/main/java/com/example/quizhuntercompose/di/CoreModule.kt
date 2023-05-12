package com.example.quizhuntercompose.di

import com.example.quizhuntercompose.core_dbo.test.TestDao
import com.example.quizhuntercompose.feature_auth.db.QuizHunterRepositoryImp
import com.example.quizhuntercompose.core_dbo.user.UserDao
import com.example.quizhuntercompose.feature_auth.domain.QuizHunterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideQuizHunterRepository(
        userDao: UserDao,
        testDao: TestDao
    ):QuizHunterRepository{
        return QuizHunterRepositoryImp(
            userDao = userDao,
            testDao = testDao
        )
    }
}