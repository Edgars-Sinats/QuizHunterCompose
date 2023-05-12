package com.example.quizhuntercompose.feature_auth.domain

import com.example.quizhuntercompose.cor.util.Response
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

typealias OneTapSignInResponse = Response<BeginSignInResult>
typealias SignInWithGoogleResponse = Response<Boolean>

interface AuthGoogleServRepository {
    val isAuthenticated: Boolean

    suspend fun oneTapSignInWithGoogle(): Flow<OneTapSignInResponse>

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<SignInWithGoogleResponse>
}