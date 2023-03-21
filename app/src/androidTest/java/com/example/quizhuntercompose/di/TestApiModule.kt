package com.example.quizhuntercompose.di

import android.content.Context
import androidx.room.Room
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
//import com.example.quizhuntercompose.feature_pickTest.db.data_source.StartingQuestions
import com.example.quizhuntercompose.feature_pickTest.db.repository.QuestionRepositoryImpl
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiModule::class]
)
object TestAppModule {


    @Singleton
    @Provides
    fun provideQuizDatabase(@ApplicationContext app: Context): QuizDatabase {
        return Room.inMemoryDatabaseBuilder(
            app.applicationContext,
            QuizDatabase::class.java
        ).build()
    }



    @Singleton
    @Provides
    fun provideQuestionRepository(db: QuizDatabase): QuestionRepository {
        return QuestionRepositoryImpl(db.questionDao)
    }

    @Singleton
    @Provides
    fun provideQuestionUseCases(repository: QuestionRepository): QuizUseCase {
        return QuizUseCase(
            getAllQuestions = GetAllQuestions(repository),
            getMyQuestions = GetMyQuestions(repository),
            getXQuestion = GetQuestions(repository),
            startTest = GetStartTest(repository),
            updateAnswer = UpdateAnswer(repository),
            getAllTopics = GetAllTopics(repository)

        )
    }

}