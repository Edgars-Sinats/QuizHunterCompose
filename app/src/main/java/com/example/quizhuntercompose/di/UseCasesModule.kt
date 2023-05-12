package com.example.quizhuntercompose.di

import com.example.quizhuntercompose.core_usecases.GetAllTestsUseCase
import com.example.quizhuntercompose.core_usecases.ProvideUserStateUseCase
import com.example.quizhuntercompose.core_usecases.QuizHunterUseCases
import com.example.quizhuntercompose.core_usecases.SignOutUseCase
import com.example.quizhuntercompose.datastore.DataStoreRepository
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
import com.example.quizhuntercompose.feature_auth.domain.QuizHunterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun providesSignOutUseCase(
        quizHunterRepository: QuizHunterRepository,
        firebaseRepository: AuthFirebaseRepository,
        dataStoreRepository: DataStoreRepository
    ): SignOutUseCase {
        return SignOutUseCase(
            firebaseRepository, quizHunterRepository, dataStoreRepository
        )
    }

    @Provides
    @Singleton
    fun providesUserStateUseCase(
        firebaseRepository: AuthFirebaseRepository,
        dataStoreRepository: DataStoreRepository,
        quizHunterRepository: QuizHunterRepository,
    ): ProvideUserStateUseCase {
        return ProvideUserStateUseCase(
            firebaseRepository, dataStoreRepository, quizHunterRepository
        )
    }

//    @Provides
//    @Singleton
//    fun provideGetAllTestsUseCase(
//        firebaseRepository: AuthFirebaseRepository,
//        quizHunterRepository: QuizHunterRepository
//    ):GetAllTestsUseCase {
//        return GetAllTestsUseCase(
//            firebaseRepository = firebaseRepository,
//            quizHunterRepository = quizHunterRepository
//        )
//    }

    @Provides
    @Singleton
    fun provideQuizHunterUseCases(
        getAllTestsUseCase: GetAllTestsUseCase,
        userStateUseCase: ProvideUserStateUseCase,
        signOutUseCase: SignOutUseCase
    ): QuizHunterUseCases{
        return QuizHunterUseCases(
            getAllTestsUseCase = getAllTestsUseCase,
            userStateProvider = userStateUseCase,
            signOut = signOutUseCase
        )
    }



}