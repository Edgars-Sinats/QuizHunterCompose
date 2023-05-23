package com.example.quizhuntercompose.core_usecases

import android.util.Log
import com.example.quizhuntercompose.cor.util.Resource
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.core_dbo.toEntity
import com.example.quizhuntercompose.domain.model.Test
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
import com.example.quizhuntercompose.feature_auth.domain.QuizHunterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllTestsUseCase @Inject constructor(
    private val quizHunterRepository: QuizHunterRepository,
    private val firebaseRepository: AuthFirebaseRepository
) {

    companion object {
        const val TAG = "GetAllTestsUseCase"
    }

    operator fun invoke(chosenLanguage: String): Flow<Resource<List<TestEntity>>> {
        return flow {
            emit(Resource.Loading())
            quizHunterRepository.getTestsLanguageEntity(chosenLanguage).collect { tests ->
                if (tests.isNotEmpty()) {
                    emit(Resource.Success(tests))
                }

                if (tests.isEmpty()) {

                    //TODO get User.language Figure out language - just String or listOfStrings()
//                    val language = "latvian"
                    Log.i(TAG, "firebaseRepository.GetTest")
                    Log.i(TAG, "language: $chosenLanguage")
                    firebaseRepository.getTests(chosenLanguage).collect() { results ->
                        when(results) {
                            is Resource.Success ->
                                results.data?.let {
                                    Log.i(TAG, "Resource.Success...")
//                                    val data = it.map { it }
//                                    Log.i(TAG, "data[1]: ${data.get(1)}")
                                    quizHunterRepository.saveTests(it) //As entity saved in Room
                                    Log.i(TAG, "savedTest ;)")
                                    emit(Resource.Success( it.toEntity() ))
                                }
                            is Resource.Error -> emit(Resource.Error(results.message ?: "unknown error"))
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}