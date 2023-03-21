package com.example.quizhuntercompose.di

import android.content.Context
import android.util.Log
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
//import com.example.quizhuntercompose.feature_pickTest.db.data_source.StartingQuestions
import com.example.quizhuntercompose.feature_pickTest.db.repository.QuestionRepositoryImpl
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.*
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
        Log.i("App Module","Database starting to build from callback as it should." )

//        return Room.databaseBuilder(
//            app,
//            QuizDatabase::class.java,
//            QuizDatabase.DATABASE_NAME
//        ).addCallback(StartingQuestions( app.applicationContext))
//            .build()

        //            .createFromAsset("huntQuestion.db")
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    db.execSQL()
//                            }
//            })


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
            getMyQuestions = GetMyQuestions(repository),
            getXQuestion = GetQuestions(repository),
            startTest = GetStartTest(repository),
            updateAnswer = UpdateAnswer(repository),
            getAllTopics = GetAllTopics(repository)

        )
    }

}