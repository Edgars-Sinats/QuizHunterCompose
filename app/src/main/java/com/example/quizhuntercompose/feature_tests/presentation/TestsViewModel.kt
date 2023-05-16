package com.example.quizhuntercompose.feature_tests.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizhuntercompose.cor.util.Resource
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.core_state.NeedKeyState
import com.example.quizhuntercompose.core_state.UserState
import com.example.quizhuntercompose.core_usecases.QuizHunterUseCases
import com.example.quizhuntercompose.feature_auth.domain.AuthFirebaseRepository
import com.example.quizhuntercompose.feature_auth.domain.QuizHunterRepository
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
        userState()
        getTests()
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

    private fun userState() {
        viewModelScope.launch(Dispatchers.IO) {
            quizHunterUseCases.userStateProvider(
                function = {}
            ).collect { userState -> _authState.value = userState}
        }
    }

    private fun getTests() {
        viewModelScope.launch(Dispatchers.IO) {
            quizHunterUseCases.getAllTestsUseCase().collect() { result ->
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

    fun openTestPreview(testId: Int){
        updateTestsState(openedTest = testId)
    }

    fun closeTestPreview(){
        updateTestsState(openedTest = null)
    }

    fun starFavoriteTest(testId: Int, setFavorite: Boolean){
        val testEntity = _state.value.tests[testId].copy(isFavorite = !setFavorite)
        Log.i(TAG, "testEntity-isFav: ${testEntity.isFavorite}")
        viewModelScope.launch(Dispatchers.IO) {
            quizHunterRepository.saveTest(testEntity)
        }

        val testsList = _state.value.tests.onEachIndexed { index, testEntity ->
            if (index == testId){
                testEntity.copy(isFavorite = setFavorite)
                return
            }
        }

        updateTestsState(tests = testsList) //TODO Is this not too much for performance, just to update boolean in db. Maybe just update _state, and when close viewModel, we could update .db
        Log.i(TAG, "testEntity-isFav, changed: ${_state.value.tests[testId].isFavorite}")

    }

    fun deleteAllTests(){
//        GlobalScope.launch(Dispatchers.IO) {
//            quizHunterRepository.removeAllTests()
//        }
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
        openedTest: Int? = null
    ) {
        _state.update {
            it.copy(
                isLoading = isLoading ?: it.isLoading,
                tests = tests ?: it.tests,
                isEmpty = isEmpty ?: it.isEmpty,
                error = error ?: it.error,
                openedTest = openedTest
            )
        }
    }

}