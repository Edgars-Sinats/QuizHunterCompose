package com.example.quizhuntercompose.core_usecases

import com.example.quizhuntercompose.datastore.DataStoreRepository
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
import com.example.quizhuntercompose.feature_auth.domain.QuizHunterRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val firebaseRepository: AuthFirebaseRepository,
    private val quizHunterRepository: QuizHunterRepository,
    private val dataStoreRepository: DataStoreRepository

    ){

    suspend operator fun invoke() {
        quizHunterRepository.removeQuizHunterUserRecord()
        quizHunterRepository.removeAllTests()
        firebaseRepository.signOut()
        dataStoreRepository.saveIsUserExist(false)
    }
}