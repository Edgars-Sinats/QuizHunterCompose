package com.example.quizhuntercompose.feature_auth.presentation_auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.example.quizhuntercompose.cor.util.Resource
import com.example.quizhuntercompose.cor.util.Response
import com.example.quizhuntercompose.cor.util.isValidEmail
import com.example.quizhuntercompose.cor.util.isValidPassword
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
import com.example.quizhuntercompose.feature_auth.domain.AuthGoogleServRepository
import com.example.quizhuntercompose.feature_auth.domain.OneTapSignInResponse
import com.example.quizhuntercompose.feature_auth.domain.SignInWithGoogleResponse
import com.example.quizhuntercompose.feature_auth.presentation_auth.state.SignInState
import com.example.quizhuntercompose.feature_auth.presentation_auth.state.SignInScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject





@HiltViewModel
class AuthViewModel @Inject constructor (
    private val firebaseRepository: AuthFirebaseRepository,
    private val googleServices: AuthGoogleServRepository,
    val oneTapClient: SignInClient
) : ViewModel(){
    val isAuthenticated = googleServices.isAuthenticated

    private val _signIn = MutableStateFlow(SignInState())
    val signIn = _signIn.asStateFlow()

    private val _screenState = MutableStateFlow(SignInScreenState())
    val screenState = _screenState.asStateFlow()

    private val _oneTapSignInResponse = MutableStateFlow<OneTapSignInResponse>(Response.Success(null))
    val oneTapSignInResponse = _oneTapSignInResponse.asStateFlow()

    private val _signInWithGoogleResponse = MutableStateFlow<SignInWithGoogleResponse>(Response.Success(false))
        val signInWithGoogleResponse = _signInWithGoogleResponse.asStateFlow()

    fun validatedSignIn() {
        if (isValidEmail(_screenState.value.email) && isValidPassword(_screenState.value.password)) {
            //TODO signIn
            signIn(
                email = _screenState.value.email,
                password = _screenState.value.password
            )
        } else {
            _screenState.update {
                it.copy(
                    isError = !isValidEmail(_screenState.value.email) || !isValidPassword(_screenState.value.password)
                )
            }
        }
    }

    private fun signIn(email: String, password: String) =
        firebaseRepository.signInWithEmailAndPassword(email, password).onEach {  result ->
            when (result) {
                is Resource.Loading -> {
                    _signIn.emit(SignInState(isLoading = true))
                    updateIsVisibleIsLoadingState(
                        isVisible = false,
                        isLoading = true
                    )

                }
                is Resource.Success -> {
                    _signIn.emit(SignInState(signIn = result.data))
                }
                is Resource.Error -> {
                    _signIn.emit(
                        SignInState(
                            error = result.message ?: "Unexpected error accrued"
                        )
                    )

                    updateIsVisibleIsLoadingState(
                        isVisible = true,
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)

    fun oneTapSignIn() = viewModelScope.launch(Dispatchers.IO) {
        googleServices.oneTapSignInWithGoogle().collect { result ->
            when (result) {
                is Response.Loading -> {
                    _oneTapSignInResponse.emit(Response.Loading)
                }
                is Response.Success -> {
                    _oneTapSignInResponse.emit(Response.Success(result.data))
                }
                is Response.Failure -> {
                    _oneTapSignInResponse.emit(Response.Failure(result.e))
                }
            }
        }
    }

    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        googleServices.firebaseSignInWithGoogle(googleCredential).collect {result ->
            when (result) {
                is Response.Loading -> {
                    _signInWithGoogleResponse.emit(Response.Loading)
                }
                is Response.Success -> {
                    _signInWithGoogleResponse.emit(Response.Success(result.data))
                }
                is Response.Failure -> {
                    _signInWithGoogleResponse.emit(Response.Failure(result.e))
                }
            }
        }
    }

    fun updateIsVisibleIsLoadingState(
        isVisible: Boolean,
        isLoading: Boolean
    ) = _screenState.update {
        it.copy(
            isVisible = isVisible,
            isLoading = isLoading
        )
    }
}