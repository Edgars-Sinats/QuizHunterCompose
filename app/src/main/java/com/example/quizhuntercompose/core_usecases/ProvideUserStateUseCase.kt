package com.example.quizhuntercompose.core_usecases

import com.example.quizhuntercompose.core_state.UserState
import com.example.quizhuntercompose.datastore.DataStoreRepository
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
import com.example.quizhuntercompose.feature_auth.domain.QuizHunterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProvideUserStateUseCase @Inject constructor(
    private val firebaseRepository: AuthFirebaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val quizHunterRepository: QuizHunterRepository
) {
    operator fun invoke(function: () -> Unit): Flow<UserState> {
        return flow {
            if (!isUserExist()) {
                emit(UserState.UnauthedUser)
                return@flow
            }

            quizHunterRepository.isUserPremiumLocal().collect { isPremium ->
                if (!isPremium) {
                    emit(UserState.AuthedUser)
                }

                if (isPremium) {
                    emit(UserState.PremiumUser)
                }
            }

            function()
        }
    }

    private suspend fun isUserExist(): Boolean {
        var doesExist = false

        dataStoreRepository.readIsUserExistState.firstOrNull()?.let {
            if (it) {
                doesExist = true
            }

            if (!it) {
                doesExist = firebaseRepository.isUserExist()
                dataStoreRepository.saveIsUserExist(doesExist)
            }
        }

        return doesExist
    }
}