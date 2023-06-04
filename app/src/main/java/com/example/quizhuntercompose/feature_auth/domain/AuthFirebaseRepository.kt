package com.example.quizhuntercompose.feature_auth.domain

import android.graphics.Bitmap
import com.example.quizhuntercompose.cor.util.Resource
import com.example.quizhuntercompose.domain.model.*
import com.example.quizhuntercompose.feature_pickTest.domain.model.FirebaseQuestion
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

/**
 * AuthFirebaseRepository manage Auth and Firebase Questions
 */
interface AuthFirebaseRepository {

    fun getUserId(): String?

//    fun getUserLanguage(): String?

    fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>>

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>>

    fun resetPasswordWithEmail(email: String): Flow<Resource<Boolean>>

    fun isCurrentUserExist(): Flow<Boolean>

    fun isUserExist(): Boolean

    fun getCurrentUserEmail(): Flow<String>

    fun signOut()

//    fun addCoinFavorite(coin: FavoriteCoin): Flow<Resource<Boolean>>
//    fun addQuestion(question: Question): Flow<Resource<Boolean>>

    fun addUserCredential(quizHunterUser: QuizHunterUser): Flow<Resource<Task<Void>>>

    fun getUserCredentials(): Flow<Resource<QuizHunterUser>>

//    fun updateUserLeaderboardState(testId: String, testResult: Int): Flow<Resource<Task<Void>>>

    fun updateUserToPremium(result: Boolean): Flow<Resource<Task<Void>>>

    fun uploadImageToCloud(name: String, bitmap: Bitmap): Flow<Resource<String>>

    fun updateUserProfilePicture(imageUrl: String): Flow<Resource<Task<Void>>>


    //TODO Change o whole document update
    fun deleteQuestion(question: Question, testId: String): Flow<Resource<Boolean>>

    fun getQuestionsFromTestId(testId: String): Flow<Resource<List<Question>>>

    fun getTest(testId: String): Flow<Resource<Test>>
    fun getTests(language: String): Flow<Resource< List<Test>>>
    fun updateTestsToCloud(testDoc: FirebaseTestDocument): Flow<Resource<Task<Void>>>

    fun updateTestToCloud(testId: String, questionList: List<FirebaseQuestion>): Flow<Resource<Task<Void>>>

    fun updateTestAnswersToCloud(testId: String, questionList: List<Question>): Flow<Resource<Task<Void>>>


    fun updateUserResultsToCloud(testId: String, answers_: List<Question>?): Flow<Resource<Task<Void>>>
}