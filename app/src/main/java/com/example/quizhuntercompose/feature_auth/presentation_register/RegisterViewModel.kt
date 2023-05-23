package com.example.quizhuntercompose.feature_auth.presentation_register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizhuntercompose.cor.util.Resource
import com.example.quizhuntercompose.domain.model.QuizHunterUser
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
import com.example.quizhuntercompose.feature_auth.domain.QuizHunterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseRepository: AuthFirebaseRepository,
    private val quizHunterRepository: QuizHunterRepository
) : ViewModel() {

    private val _signUp = MutableStateFlow(SignUpState())
    val signUp: StateFlow<SignUpState> = _signUp

    fun signUp(quizHunterUser: QuizHunterUser, password: String) =
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "RegisterView signUp")
            firebaseRepository.signUpWithEmailAndPassword(quizHunterUser.email!!, password).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _signUp.emit(SignUpState(signUp = result.data))
                    }
                    is Resource.Error -> {
                        _signUp.emit(
                            SignUpState(
                                error = result.message ?: "Unexpected Error"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _signUp.emit(SignUpState(isLoading = true))
                    }
                }
            }
        }

    fun addUserCredential(quizHunterUser: QuizHunterUser) =
        firebaseRepository.addUserCredential(quizHunterUser).onEach { result ->
            when (result) {
                is Resource.Success -> { quizHunterRepository.saveQuizHunterUser(quizHunterUser) }
                else -> {}
            }

        }.launchIn(viewModelScope)
}