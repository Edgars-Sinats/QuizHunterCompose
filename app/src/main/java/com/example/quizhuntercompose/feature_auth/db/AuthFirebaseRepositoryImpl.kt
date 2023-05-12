package com.example.quizhuntercompose.feature_auth.db

import android.graphics.Bitmap
import android.util.Log
import com.example.quizhuntercompose.cor.util.AppConstants
import com.example.quizhuntercompose.cor.util.Resource
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.domain.model.*
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
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

    override fun getTests(language: String): Flow<Resource<List<Test>>> {
        return callbackFlow {
            try {
                Log.i(TAG, "Trying loading... ")
                this.trySend(Resource.Loading())
                val snapshot = fireStore.collection(AppConstants.TEST_COLLECTION)
                    .document(language)
                Log.i(TAG, "Snapshot is taken... ")

                snapshot.addSnapshotListener {value, error ->

                    error?.let {
                        Log.i(TAG, "Firebase error in snapshot listener ")
                        this.close(it)
                    }
                    value?.let { testsFirebaseData1 ->
                        Log.i(TAG, " \n testsFirebaseData1: ${testsFirebaseData1}")
                        Log.i(TAG, " \n testsFirebaseData1 data: ${testsFirebaseData1.data}")
                        val newDoc = testsFirebaseData1.toObject(FirebaseTestDocument::class.java)
                        Log.i(TAG, " \n newDoc values: ${newDoc?.documentTests?.values }")
                        Log.i(TAG, " \n newDoc doc: ${newDoc?.documentTests }")
                        Log.i(TAG, " \n newDoc entries: ${newDoc?.documentTests?.entries }")

                        var testsRoom: Map<String, TestFirebase>
                        testsRoom = newDoc?.documentTests ?: emptyMap()
                        Log.i(TAG, " \n testRoom element: ${testsRoom }")
                        Log.i(TAG, " \n testRoom values: ${testsRoom.values }")
//                        testsFirebaseData1.data?.entries?.associate {
//                            Log.i(TAG, " \n testFirebase key: ${it.key }")
//                            Log.i(TAG, " \n testFirebase value: $it")
//                            it.key to testsRoom.entries.add(element = <it.key, it.value>)
//                            it.value to testsRoom.values;
//                        }


//                        val testsFirebaseDataObject = testsFirebaseData1 as (FirebaseTestDocument2::class.java)
//                        var testsRoom1: Map<String, TestFirebase>? = testsFirebaseDataObject?.doc
////                        testsRoom1.entries = testsFirebaseDataObject.doc
//                        Log.i(TAG, " \n testRoom values: ${testsRoom1?.values }")


//                        value.data.ass
                        val testsFirebaseData = value.data
                        testsFirebaseData?.let {

                            Log.i(TAG, "Succesfully got firebase data \n DATA:  ${testsFirebaseData.toString()} ")
                            Log.i(TAG, " Line 289 \n First element: ${testsFirebaseData?.get("1") }")
                            Log.i(TAG, " Line 289 \n Second element: ${testsFirebaseData?.get("2") }")
                            Log.i(TAG, " Line 289 \n Size of element: ${testsFirebaseData?.size }")


//                            testsFirebaseData?.let { firebaseDocMap ->

//                            Log.i(TAG, "firebaseDocMap value: ${firebaseDocMap}")
//
//                            Log.i(TAG, "firebaseDocMap value: ${firebaseDocMap}")

                            val entityDocMap = mutableMapOf<String, Test>()
                                var count = 0
                            val docSize = newDoc?.documentTests?.entries?.size
                            val myDoc = newDoc?.documentTests

//                            var test: Test = Test(testID = 1, testName = "A", dateCreated = "", needKey = false, language = "", testRank = 1)
                                testsFirebaseData.forEach { v1 ->

                                    val as12 = testsFirebaseData.values.forEach { fTestObject ->
//                                        fTestObject as TestFirebase
//                                        Log.i(TAG, "fTestObject values: ${fTestObject}")
//                                        test = Test(
//
//                                            testID = fTestObject.testID,
//                                            testName =fTestObject.testName,
//                                            dateCreated = fTestObject.dateCreated.toString(),
//                                            dateModified = fTestObject.dateModified.toString() ?: "",
//                                            testImageUrl = fTestObject.testImageUrl ?: "",
//                                            language = fTestObject.language ?: "",
//                                            testDescription = fTestObject.testDescription,
//                                            needKey = fTestObject.needKey,
//                                            additionalInfo =fTestObject.additionalInfo,
//                                            testRank = fTestObject.testRank,
//                                            isFavorite = fTestObject.isFavorite
//                                        )
                                    }
                                    Log.i(TAG, "as12: ${as12}")

//                                    val test = Test(
//                                        testID = dat2.get(count.toString())?.testID ?: 0,
//                                        testName = dat2.get(count.toString())?.testName?: "",
//                                        dateCreated = dat2.get(count.toString())?.dateCreated.toString(),
//                                        dateModified = dat2.get(count.toString())?.dateModified.toString(),
//                                        testImageUrl = dat2.get(count.toString())?.testImageUrl,
//                                        language = dat2.get(count.toString())?.language?: "",
//                                        testDescription = dat2.get(count.toString())?.testDescription?: "",
//                                        needKey = dat2.get(count.toString())?.needKey?: false,
//                                        additionalInfo = dat2.get(count.toString())?.additionalInfo,
//                                        testRank =  dat2.get(count.toString())?.testRank?:1,
//                                        isFavorite = dat2.get(count.toString())?.isFavorite?:false,
//                                    )

//                                    entityDocMap[count.toString()] = test
                                    count++
                                    println("firebaseDocMap count: $count")
                                    println(entityDocMap)
                                }
                        }

//                        testsRoom

                        Log.i(TAG, " Line 286 ")
//                        val fTests: FirebaseTestDocument? = value.toObject(FirebaseTestDocument::class.java)
//                        Log.i(TAG, " Line 289 \n fTests: ${fTests?.doc}")

//                        val fTests2: FirebaseTestDocument2? = value.toObject(FirebaseTestDocument2::class.java) //no setter found
//                        Log.i(TAG, " Line 291 \n fTests: ${fTests2?.doc}")

//                        val m1: Map<String, TestFirebase>? = value.toObject()
//                        Log.i(TAG, " Line 292 \n m1: ${m1?.values}")




                        var firebaseTestList = FirebaseTestList( emptyList() )
//                        val m2: Map<String, Test> = m1?.mapValues { it.value.copy(m1.values)
//                            Test(
//                                isFavorite = it.value.isFavorite,
//                                testRank = it.value.testRank,
//                                testDescription = a1.testDescription,
//                                additionalInfo = a1.additionalInfo,
//                                needKey = a1.needKey,
//                                language = a1.language,
//                                testName = a1.testName,
//                                testImageUrl = a1.testImageUrl,
//                                dateModified = a1.dateModified.toString(),
//                                dateCreated = a1.dateCreated.toString(),
//                                testID = a1.testID
//                            )
//                        } ?: emptyMap()

                        Log.i(TAG, " Line 402  testRoom : $testsRoom ")
                        Log.i(TAG, " Line 408 test Room values: ${testsRoom.values} ")
                        Log.i(TAG, " Line 408 test Room entries: ${testsRoom.entries} ")

                        var testsToSend: MutableList<Test> = mutableListOf()
                        var testsToSend1: MutableList<Test> = mutableListOf()


                        var iteratorCount = 0
                        var iteratorCount1 = 0


                        testsRoom.values.map {
                            testsToSend1.add(
                                iteratorCount1, Test(
                                    isFavorite = it.isFavorite,
                                    testRank = it.testRank,
                                    testDescription = it.testDescription,
                                    additionalInfo = it.additionalInfo,
                                    needKey = it.needKey,
                                    language = it.language,
                                    testName = it.testName,
                                    testImageUrl = it.testImageUrl,
                                    dateModified = it.dateModified.toString(),
                                    dateCreated = it.dateCreated.toString(),
                                    testID = it.testID
                                )
                            )
                            iteratorCount1++
                        }
                        Log.i(TAG, " 434 - testsToSend1: ${testsToSend1} ")



                        testsRoom.entries.map {
                            testsToSend.add(
                                iteratorCount, Test(
                                    isFavorite = it.value.isFavorite,
                                    testRank = it.value.testRank,
                                    testDescription = it.value.testDescription,
                                    additionalInfo = it.value.additionalInfo,
                                    needKey = it.value.needKey,
                                    language = it.value.language,
                                    testName = it.value.testName,
                                    testImageUrl = it.value.testImageUrl,
                                    dateModified = it.value.dateModified.toString(),
                                    dateCreated = it.value.dateCreated.toString(),
                                    testID = it.value.testID
                                )
                            )
                            iteratorCount++
                        }

                        Log.i(TAG, " Line 402  testsToSend: ${testsToSend} ")
                        Log.i(TAG, " Line 402  testsToSend.toList(): ${testsToSend.toList()} ")

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
            val document = "latvian"
            val update = fireStore.collection(AppConstants.TEST_COLLECTION)
                .document(document)
                .set(testDoc)

            Log.i(TAG, "updating Firebase! ")
            update.await()
            Log.i(TAG, "Should be success! ")

            emit(Resource.Success(update))
        }.catch {
            Log.i(TAG, "Error caught: $it")
            emit(Resource.Error(it.toString()))
        }
    }

    override fun updateTestToCloud(testId: String): Flow<Resource<Task<Void>>> {
        return flow {
            emit(Resource.Loading())
            //TODO get test from Room
            val update = fireStore.collection(AppConstants.QUESTION_COLLECTION)
                .document(testId)
                .set("getQuestionsWhereTestIds = testId")

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