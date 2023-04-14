package com.example.quizhuntercompose.di

import android.app.Application
import android.content.Context
import com.example.quizhuntercompose.feature_auth.db.AuthRepositoryImpl
import com.example.quizhuntercompose.feature_auth.domain.AuthRepository
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
//import com.example.quizhuntercompose.feature_pickTest.db.data_source.StartingQuestions
import com.example.quizhuntercompose.feature_pickTest.db.repository.QuestionRepositoryImpl
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.*
import com.example.quizhuntercompose.cor.util.AppConstants.SIGN_IN_REQUEST
import com.example.quizhuntercompose.cor.util.AppConstants.SIGN_UP_REQUEST
import com.example.quizhuntercompose.feature_auth.db.ProfileRepositoryImpl
import com.example.quizhuntercompose.feature_auth.domain.ProfileRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext // Depricated
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Named


//class AppModule{
//
//}
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirebaseDatabase() = Firebase.database

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ) = Identity.getSignInClient(context)

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun provideSignInRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build())
            .setAutoSelectEnabled(true)
            .build()

    @Provides
    @Named(SIGN_UP_REQUEST)
    fun provideSignUpRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build())
        .build()

    @Provides
    fun provideGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.web_client_id))
        .requestEmail()
        .build()

    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)


//    @Singleton
    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest,
        db: FirebaseFirestore
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signUpRequest = signUpRequest,
        db = db
    )

    @Provides
    fun provideProfileRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        signInClient: GoogleSignInClient,
        db: FirebaseFirestore
    ): ProfileRepository = ProfileRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInClient = signInClient,
        db = db
    )

    @Singleton
    @Provides
    fun provideQuizDatabase(@ApplicationContext app: Context): QuizDatabase {
        return QuizDatabase.getDatabase(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(db: QuizDatabase): QuestionRepository {
        return QuestionRepositoryImpl(db.questionDao)
    }

    @Provides
    @Singleton
    fun provideQuestionUseCases(repository: QuestionRepository): QuizUseCase {
        return QuizUseCase(
            getAllQuestions = GetAllQuestions(repository),
            getMyQuestions = GetMyQuestions(repository),
            getXQuestion = GetQuestions(repository),
            startTest = GetStartTest(repository),
            updateAnswer = UpdateAnswer(repository),
            getAllTopics = GetAllTopics(repository)

        )
    }

}