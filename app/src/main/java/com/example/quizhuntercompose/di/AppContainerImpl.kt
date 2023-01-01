package com.example.quizhuntercompose.di

import android.content.Context
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuestionDao
import com.example.quizhuntercompose.feature_pickTest.db.repository.QuestionRepositoryImpl
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val questionRepository: QuestionRepository
    //TODO val
    //TODO val userRepository: UserRepository
}

//class AppContainerImpl(private val applicationContext: Context) : AppContainer{
//    override val questionRepository: QuestionRepository by lazy {
//        QuestionRepositoryImpl(QuestionDao.getQuestionX(null)) //Myb App module
//    }
////        get() = TODO("Not yet implemented")
//}