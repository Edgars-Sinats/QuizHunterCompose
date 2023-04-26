package com.example.quizhuntercompose.feature_auth.db

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.quizhuntercompose.cor.util.AppConstants.SIGN_IN_REQUEST
import com.example.quizhuntercompose.cor.util.AppConstants.SIGN_UP_REQUEST
import com.example.quizhuntercompose.cor.util.FirebaseConstants.CREATED_AT
import com.example.quizhuntercompose.cor.util.FirebaseConstants.DISPLAY_NAME
import com.example.quizhuntercompose.cor.util.FirebaseConstants.EMAIL
import com.example.quizhuntercompose.cor.util.FirebaseConstants.PHOTO_URL
import com.example.quizhuntercompose.cor.util.FirebaseConstants.USERS
import com.example.quizhuntercompose.cor.util.Response

import com.example.quizhuntercompose.feature_auth.domain.AuthGoogleServRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthGoogleServRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore
) : AuthGoogleServRepository {
    override val isAuthenticated = auth.currentUser != null

    override suspend fun oneTapSignInWithGoogle() = flow {
         try {
             emit(Response.Loading)

            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            emit(Response.Success(signInResult))
        } catch (e: Exception) {
            try {
                val signUpRequest = oneTapClient.beginSignIn(signUpRequest).await()
                emit(Response.Success(signUpRequest))
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential) = flow {
        try {
            emit(Response.Loading)
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                createUserInFirestore()
            }
            emit(Response.Success(true))
        } catch (e: java.lang.Exception) {
            emit(Response.Failure(e))
        }
    }

    private suspend fun createUserInFirestore(){
        auth.currentUser?.apply {
            val user = toUser()
            db.collection(USERS).document(uid).set(user).await()
        }
    }
}

fun FirebaseUser.toUser() = mapOf(
    CREATED_AT to serverTimestamp(),
    DISPLAY_NAME to displayName,
    EMAIL to email,
    PHOTO_URL to photoUrl?.toString()
)