package com.example.quizhuntercompose.core_usecases

import android.util.Log
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
//                Log.i(TAG, "isUserExit(): ${isUserExist()}")
                emit(UserState.UnauthedUser)
                return@flow
            }

            quizHunterRepository.isUserPremiumLocal().collect { isPremium ->
                if (!isPremium) {
                    Log.i(TAG, "Final - quizHunterRepository is authed : ${!isPremium}" )
                    emit(UserState.AuthedUser)
                }

                if (isPremium) {
                    Log.i(TAG, "Final - quizHunterRepository is (premium) user: ${!isPremium}" )
                    emit(UserState.PremiumUser)
                }
            }

            function()
        }
    }

    private suspend fun isUserExist(): Boolean {
        var doesExist = false

        dataStoreRepository.readIsUserExistState.firstOrNull()?.let {
            Log.i(TAG, "dataStoreRepository-readIsUserExistState it: $it" )
            if (it) {
                doesExist = true
            }

            if (!it) {
                doesExist = firebaseRepository.isUserExist()
                Log.i(TAG, " firebaseRepository.isUserExist: $doesExist" )
                dataStoreRepository.saveIsUserExist(doesExist)
            }
        }
        Log.i(TAG, "returning doesExit: $doesExist")

        return doesExist
    }

    companion object {
        private const val TAG = "ProvideUserStateUseCase"
    }
}