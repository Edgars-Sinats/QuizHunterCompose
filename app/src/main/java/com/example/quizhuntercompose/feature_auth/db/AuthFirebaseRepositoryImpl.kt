package com.example.quizhuntercompose.feature_auth.db

import android.graphics.Bitmap
import android.util.Log
import com.example.quizhuntercompose.cor.util.AppConstants
import com.example.quizhuntercompose.cor.util.Resource
import com.example.quizhuntercompose.core_dbo.toDomainQuestion
import com.example.quizhuntercompose.domain.model.*
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
import com.example.quizhuntercompose.feature_pickTest.domain.model.FirebaseQuestion
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.IOException

class AuthFirebaseRepositoryImpl constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : AuthFirebaseRepository {

    companion object {
        const val TAG = "authFirebaseRepImp"
    }

    override fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

//    override fun getUserLanguage(): String? {
//        return fireStore.
//    }

    override fun signUpWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                )
            )
        }.catch {
            emit(Resource.Error( it.toString() ) )
        }

    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())

            val result = firebaseAuth.signInWithEmailAndPassword(
                email,
                password
            ).await()

            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun resetPasswordWithEmail(email: String): Flow<Resource<Boolean>> {
        return callbackFlow {
            this.trySend(Resource.Loading())
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener{ task ->
                    when {
                        task.isSuccessful -> {
                            this.trySend(Resource.Success(true))
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    exception.message?.let {
                        trySend(Resource.Error(it))
                    }
                }
            awaitClose{ this.cancel() }
        }
    }

    override fun isCurrentUserExist(): Flow<Boolean> {
        return flow {
            emit(firebaseAuth.currentUser != null)
        }
    }

    override fun isUserExist(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUserEmail(): Flow<String> {
        return flow {
            firebaseAuth.currentUser?.email?.let {
                emit(it)
            }
        }
    }

    override fun signOut() {
        return firebaseAuth.signOut()
    }

    override fun addUserCredential(quizHunterUser: QuizHunterUser): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userUid ->
                val userRef = fireStore.collection(AppConstants.USER_COLLECTION)
                    .document(userUid)
                    .set(quizHunterUser)

                userRef.await()
                emit(Resource.Success(userRef))
            }
        }.catch {
            emit(Resource.Error(it.message ?: "Unexpected Message"))
        }
    }

    override fun getUserCredentials(): Flow<Resource<QuizHunterUser>> {
        return callbackFlow {
            this.trySend(Resource.Loading())
            getUserId()?.let { userId ->
                val snapShot =
                    fireStore.collection(AppConstants.USER_COLLECTION)
                        .document(userId)
                snapShot.addSnapshotListener { value, error ->
                    error?.let {
                        this.trySend(Resource.Error(it.message.toString()))
                        this.close(it)
                    }

                    value?.let {
                        val data = value.toObject(QuizHunterUser::class.java)
                        this.trySend(Resource.Success(data!!))
                    }
                }
            }
            awaitClose { this.cancel() }
        }
    }

    override fun updateUserToPremium(result: Boolean): Flow<Resource<Task<Void>>> {
        return flow {
            getUserId()?.let { userUid ->
                emit(Resource.Loading())
                val favoriteRef =
                    fireStore.collection(AppConstants.USER_COLLECTION)
                        .document(userUid)
                        .update("premium", result)

                favoriteRef.await()
                emit(Resource.Success(favoriteRef))

            }
        }.catch {
            emit(Resource.Error(it.toString()))
        }
    }

    override fun uploadImageToCloud(
        name: String,
        bitmap: Bitmap
    ): Flow<Resource<String>> {
        return callbackFlow {
            this.trySend(Resource.Loading())

            val storageRef = firebaseStorage.reference
            val imageRef = storageRef.child("images/$name.jpeg")

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val data = byteArrayOutputStream.toByteArray()

            val uploadTask = imageRef.putBytes(data)

            uploadTask
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            this.trySend(Resource.Error(it.message.toString()))
                            this.close(it)
                        }
                    }

                    imageRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        if (downloadUri == null) {
                            this.trySend(Resource.Error("Invalid image URL"))
                            this.close(null)
                        } else {
                            this.trySend(Resource.Success(downloadUri.toString()))
                        }
                    } else {
                        this.trySend(Resource.Error(task.exception?.message.toString()))
                        this.close(task.exception)
                    }
                }

            awaitClose { this.cancel() }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun updateUserProfilePicture(imageUrl: String): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            getUserId()?.let { userId ->
                val imageRef = fireStore.collection(AppConstants.USER_COLLECTION)
                    .document(userId)
                    .update("image", imageUrl)

                imageRef.await()
                emit(Resource.Success(imageRef))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    //TODO
    // Not good idea, update whole document with all questions instead of this one.
    //Only available for admin || teacher.
    override fun deleteQuestion(question: Question, testId: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            //TODO check if user admin
            fireStore.collection(AppConstants.QUESTION_COLLECTION)
                .document(testId)
                .update("question_field", question)
                .await()
            emit(Resource.Success(true))
        }.catch {
            emit(Resource.Error(it.message?:"Unexpected message"))
        }
    }

    //
    override fun getQuestionsFromTestId(testId: String): Flow<Resource<List<Question>>> {
        return callbackFlow {
            this.trySend(Resource.Loading())

        }
    }

    //TODO clean this func
    override fun getTests(language: String): Flow<Resource<List<Test>>> {
        return callbackFlow {
            try {
                Log.i(TAG, "Trying loading... ")
                this.trySend(Resource.Loading())
                val snapshot = fireStore.collection(AppConstants.TEST_COLLECTION)
                    .document(language)
                Log.i(TAG, "Snapshot is taken... ")

                snapshot.addSnapshotListener { value, error ->

                    error?.let {
                        Log.i(TAG, "Firebase error in snapshot listener ")
                        this.close(it)
                    }
                    value?.let { testsFirebaseData1 ->
                        val newDoc = testsFirebaseData1.toObject(FirebaseTestDocument::class.java)
                        val testsRoom: Map<String, TestFirebase> = newDoc?.documentTests ?: emptyMap()

                        Log.i(TAG, " \n testsFirebaseData1: ${testsFirebaseData1}")
                        Log.i(TAG, " \n testsFirebaseData1 data: ${testsFirebaseData1.data}")
                        Log.i(TAG, " \n newDoc values: ${newDoc?.documentTests?.values }")
                        Log.i(TAG, " \n newDoc entries: ${newDoc?.documentTests?.entries }")
                        Log.i(TAG, " \n testRoom element: ${testsRoom }")
                        Log.i(TAG, " \n testRoom values: ${testsRoom.values }")

//                        val testsFirebaseDataObject = testsFirebaseData1 as (FirebaseTestDocument2::class.java)
//                        var testsRoom1: Map<String, TestFirebase>? = testsFirebaseDataObject?.doc

                        val testsFirebaseData = value.data
                        testsFirebaseData?.let {

                            Log.i(TAG, "Succesfully got firebase data \n DATA:  ${testsFirebaseData.toString()} ")
                            Log.i(TAG, " Line 289 \n First element: ${testsFirebaseData?.get("1") }")
                            Log.i(TAG, " Line 289 \n Second element: ${testsFirebaseData?.get("2") }")
                            Log.i(TAG, " Line 289 \n Size of element: ${testsFirebaseData?.size }")

                            val entityDocMap = mutableMapOf<String, Test>()
                                var count = 0
                                testsFirebaseData.forEach {
//                                    entityDocMap[count.toString()] = test
                                    count++
                                    println("firebaseDocMap count: $count")
                                    println(entityDocMap)
                                }
                        }

                        var testsToSend: MutableList<Test> = mutableListOf()
                        var iteratorCount = 0

                        testsRoom.values.map {
                            testsToSend.add(
                                iteratorCount, Test(
                                    isFavorite = it.isFavorite,
                                    testRank = it.testRank,
                                    testDescription = it.testDescription,
                                    additionalInfo = it.additionalInfo,
                                    needKey = it.needKey,
                                    language = it.language,
                                    testName = it.testName,
                                    testImageUrl = it.testImageUrl,
                                    dateModified = it.dateModified?.seconds ?: 0,
                                    //TODO add nanoseconds for time modified.
                                    dateCreated = it.dateCreated?.seconds ?: 0,
                                    testID = it.testID
                                )
                            )
                            iteratorCount++
                        }

                        Log.i(TAG, "   testsToSend.toList(): ${testsToSend.toList()} ")

                        this.trySend(Resource.Success(testsToSend.toList()))
                    }
                }
            }
            catch (e: IOException) {
                trySend(Resource.Error("Couldn't reach server. Check your internet connection"))
                close(e)
            }

            awaitClose{this.cancel()}
        }
    }

    override fun getTest(testId: String): Flow<Resource<Test>> {
        return callbackFlow {
            this.trySend(Resource.Loading())
            val test =
                fireStore.collection(AppConstants.QUESTION_COLLECTION)
                    .document(testId)
            test.addSnapshotListener { value, error ->
                error?.let {
                    this.trySend(Resource.Error(it.message.toString()))
                    this.close(it)
                }

                value?.let {
                    val data = value.toObject(Test::class.java)
                    this.trySend(Resource.Success(data!!))
                }
            }
            awaitClose{this.cancel()}
        }
    }

    override fun updateTestsToCloud(testDoc: FirebaseTestDocument): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            val document = "latvian" //TODO
            val update = fireStore.collection(AppConstants.TEST_COLLECTION)
                .document(document)
                .set(testDoc)
            update.await()
            emit(Resource.Success(update))

        }.catch {
            Log.i(TAG, "Error caught: $it")
            emit(Resource.Error(it.toString()))
        }
    }

    override fun updateTestToCloud(testId: String, questionList: List<FirebaseQuestion>): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())

            Log.i(TAG, "setting firebaseTestQuestions object")

            val firebaseTestQuestions: MutableMap<String, FirebaseQuestion> = mutableMapOf()
            Log.i(TAG, "Setting question List...")

            questionList.forEach { question ->
                firebaseTestQuestions.put(key = question.questionID.toString(), value = question)
            }

            Log.i(TAG, "Uploading questions...")
            val update = fireStore.collection(AppConstants.QUESTION_COLLECTION)
                .document(testId)
                .set(firebaseTestQuestions)

            update.await()
            Log.i(TAG, "Questions uploaded :) \n update: $update ")
            emit(Resource.Success(update))
        }.catch {
            emit(Resource.Error(it.toString()))

        }
    }

    override fun updateTestAnswersToCloud(testId: String, questionList: List<Question>): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            //TODO get test from Room
            val update = fireStore.collection(AppConstants.QUESTION_COLLECTION)
                .document(testId)
                .set(questionList.toDomainQuestion().second)

            update.await()
            emit(Resource.Success(update))
        }.catch {
            emit(Resource.Error(it.toString()))

        }
    }

    override fun updateUserResultsToCloud(
        testId: String,
        answers_: List<Question>?,
    ): Flow<Resource<Task<Void>>> {
        return flow {
            getUserId()?.let { userId ->
                emit(Resource.Loading())
                val updatedAnswers =
                    fireStore.collection(AppConstants.ANSWER_COLLECTION)
                        .document(userId)
                        .update(testId, answers_)

                updatedAnswers.await()
                emit(Resource.Success(updatedAnswers))
            }
        }.catch {
            emit(Resource.Error(it.toString()))
        }
    }
}