package com.example.quizhuntercompose.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
import com.example.quizhuntercompose.feature_pickTest.db.data_source.StartingQuestions
import com.example.quizhuntercompose.feature_pickTest.db.repository.QuestionRepositoryImpl
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuizDatabase(app: Application): QuizDatabase {
        Log.i("App Module","Database starting to build from callback as it should." )

        return Room.databaseBuilder(
            app,
            QuizDatabase::class.java,
            QuizDatabase.DATABASE_NAME
        ).addCallback(StartingQuestions( app.applicationContext))
//            .createFromAsset("huntQuestion.db")
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    db.execSQL()
//                            }
//            })
            .build()

    }

    @Provides
    @Singleton
    fun provideQuestionRepository(db: QuizDatabase): QuestionRepository {
        return QuestionRepositoryImpl(db.questionDao)
    }

    @Provides
    @Singleton
    fun provideQuestionUseCases(repository: QuestionRepository): QuizUseCase {
        return QuizUseCase(
            getAllQuestions = GetAllQuestions(repository),
            getXQuestion = GetQuestions(repository),
            startTest = GetStartTest(repository),
            updateAnswer = UpdateAnswer(repository),
            getAllTopics = GetAllTopics(repository)

        )
    }

}