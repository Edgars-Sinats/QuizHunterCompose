package com.example.quizhuntercompose.feature_tests.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizhuntercompose.cor.util.Resource
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.core_dbo.toDomainQuestion
import com.example.quizhuntercompose.core_state.NeedKeyState
import com.example.quizhuntercompose.core_state.UserState
import com.example.quizhuntercompose.core_usecases.QuizHunterUseCases
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
import com.example.quizhuntercompose.feature_auth.domain.QuizHunterRepository
import com.example.quizhuntercompose.feature_pickTest.domain.model.FirebaseQuestion
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_tests.presentation.components.ExperimentalTests
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestsViewModel @Inject constructor(
    private val quizHunterUseCases: QuizHunterUseCases,
    private val quizHunterRepository: QuizHunterRepository,
    private val firebaseRepository: AuthFirebaseRepository,
    private val questionRepository: QuestionRepository
) :ViewModel() {

    private val TAG = "testViewModel"

    private val _state = MutableStateFlow(TestsState())
    val state: StateFlow<TestsState> = _state.asStateFlow()

    private val _authState = mutableStateOf<UserState>(UserState.UnauthedUser)
    val authState: State<UserState> = _authState

    private val _sideEffect = mutableStateOf(false)
    val sideEffect: State<Boolean> = _sideEffect

    private val _needKeyState = mutableStateOf<NeedKeyState>(NeedKeyState.NotKey)
    val needKeyState: State<NeedKeyState> = _needKeyState

    private val _needKeyMsg = mutableStateOf(NeedKeyState.Messages())
    val needKeyMsg : State<NeedKeyState.Messages> = _needKeyMsg

    init {
//        userState()
//        getTests()
    }

    fun uploadTests(){
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.updateTestsToCloud(
                ExperimentalTests()
            ).collect{
                when(it) {
                    is Resource.Loading -> updateTestsState(isLoading = true)
                    is Resource.Success -> {
                        it.data?.let { data ->

                            Log.i("TAG VIEWMODEL", "on Success, !?!?!?!?!?")
//                            updateTestsState(tests = data.result.toString())
                        }
                    }
                    is Resource.Error -> updateTestsState(error = it.message)
                }
            }
        }
    }

    fun getLanguage(language: String){
        updateTestsState(language = language)
    }

    fun userState() {
        viewModelScope.launch(Dispatchers.IO) {
            quizHunterUseCases.userStateProvider(
                function = {}
            ).collect { userState ->
                _authState.value = userState
            }
        }
    }

    fun getTests() {
        viewModelScope.launch(Dispatchers.IO) {
            quizHunterUseCases.getAllTestsUseCase(chosenLanguage = _state.value.language ?: "english").collect() { result ->
                when (result) {
                    is Resource.Loading -> updateTestsState(isLoading = true)
                    is Resource.Success -> {
                        result.data?.let { data ->
//                            val state = data.map { TestsState().copy(tests = it) }

                            updateTestsState(tests = data)
                        }
                    }
                    is Resource.Error -> updateTestsState(error = result.message)
                }
            }
        }
    }

    fun openTestPreview(test: TestEntity){
        updateTestsState(openedTest = test)
    }

    fun closeTestPreview(){
        updateTestsState(openedTest = null)
    }

    //experimental upload to Firebase
    //
    fun uploadTestToFirebase(forTestId: TestEntity){
        val testID = _state.value.openedTest!!.testId

        viewModelScope.launch(Dispatchers.IO) {

            val questionList = questionRepository.getAllQuestions(testID)

            Log.i(TAG, "updateTestToCloud...")
            firebaseRepository.updateTestToCloud(testID.toString(), questionList.toDomainQuestion().first).collect() {
                when (it) {
                    is Resource.Loading -> updateTestsState(isLoading = true)

                    is Resource.Success -> {
                        it.data?.let { _ ->
    //                            val state = data.map { TestsState().copy(tests = it) }

                            updateTestsState(isLoading = false)
                        }
                    }
                    is Resource.Error -> updateTestsState(error = it.message)
                }
            }
        }
    }
    //Open ONLY When Test Dialog Is Opened
    fun getAllLocalTestQuestions(){
        val testID = _state.value.openedTest!!.testId
        questionRepository.getAllQuestions(testID)
    }

    fun starFavoriteTest(testId: TestEntity){
        var testsList1 = _state.value.tests.toMutableList()
        var savableTest: TestEntity
        var count = 0
        _state.value.tests.forEach { thisTestEntity ->
            if (thisTestEntity.testId == testId.testId){
                savableTest = thisTestEntity.copy(isFavorite = testId.isFavorite.not())
                testsList1[count] = testsList1.get(count).copy(isFavorite = testId.isFavorite.not())
                Log.i(TAG, "testEntity-isFav: ${thisTestEntity.isFavorite}")
                Log.i(TAG, "testEntity-count: ${count}")
                Log.i(TAG, "This new changed object: ${testsList1[count]}")
                Log.i(TAG, "Inverse boolean of this object: ${testsList1[count].isFavorite.not()}")

                viewModelScope.launch(Dispatchers.IO) {
                    quizHunterRepository.saveTest(savableTest)
                }
                updateTestsState(tests = testsList1)
            }
            count++
        }
    }

    fun deleteAllTests(){
        viewModelScope.launch(Dispatchers.IO) {
            quizHunterRepository.removeAllTests()
        }
        updateTestsState()
    }

    private fun getChosenTest() {
    }

    private fun backToTop(){
    }

    private fun updateTestsState(
        isLoading: Boolean? = null,
        tests: List<TestEntity>? = null,
        isEmpty: Boolean? = null,
        error: String? = null,
        openedTest: TestEntity? = null,
        language: String? = null
    ) {
        _state.update {
            it.copy(
                isLoading = isLoading ?: it.isLoading,
                tests = tests ?: it.tests,
                isEmpty = isEmpty ?: it.isEmpty,
                error = error ?: it.error,
                openedTest = openedTest,
                language = language ?: it.language
            )
        }
    }

}