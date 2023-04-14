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
import com.example.quizhuntercompose.domain.model.Response.Failure
import com.example.quizhuntercompose.domain.model.Response.Success
import com.example.quizhuntercompose.feature_auth.domain.AuthRepository
import com.example.quizhuntercompose.feature_auth.domain.OneTapSignInResponse
import com.example.quizhuntercompose.feature_auth.domain.SignInWithGoogleResponse
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore
) : AuthRepository {
    override val isAuthenticated = auth.currentUser != null

    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpRequest = oneTapClient.beginSignIn(signUpRequest).await()
                Success(signUpRequest)
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse {
        return try {

            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                createUserInFirestore()
            }
            Success(true)
        } catch (e: java.lang.Exception) {
            Failure(e)
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