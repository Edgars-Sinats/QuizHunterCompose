package com.example.quizhuntercompose.di

import android.content.Context
import com.example.quizhuntercompose.core_dbo.test.TestDao
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
//import com.example.quizhuntercompose.feature_pickTest.db.data_source.StartingQuestions
import com.example.quizhuntercompose.feature_pickTest.db.repository.QuestionRepositoryImpl
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.*
import com.example.quizhuntercompose.core_dbo.user.UserDao
//import com.google.firebase.database.ktx.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext // Depricated
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//class AppModule{
//
//}
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideQuizDatabase(@ApplicationContext app: Context): QuizDatabase {
        return QuizDatabase.getDatabase(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(
        db: QuizDatabase
    ): QuestionRepository {
        return QuestionRepositoryImpl(db.questionDao)
    }



    @Provides
    @Singleton
    fun provideUserDao(
        database: QuizDatabase
    ): UserDao {
        return database.userDao
    }

    @Provides
    @Singleton
    fun provideTestDao(
        database: QuizDatabase
    ): TestDao {
        return database.testDao
    }

    @Provides
    @Singleton
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