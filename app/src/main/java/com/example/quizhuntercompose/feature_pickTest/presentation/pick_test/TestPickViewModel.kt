package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.QuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestPickViewModel @Inject constructor(
//    private val quizUseCase: QuizUseCase,
    private val questionRepository: @JvmSuppressWildcards QuestionRepository,

    savedStateHandle: @JvmSuppressWildcards SavedStateHandle,
//    onNavigationRequested: @JvmSuppressWildcards (itemId: String) -> Unit
) : ViewModel() {

    //For now, replace with barPicker
    private val _quizPickOptions = TestPickOptionsState(  ) //It is 5 already inside

    private val _quizPickOptionsState = mutableStateOf(TestPickOptionsState())
    var quizPickOptions: State<TestPickOptionsState> = _quizPickOptionsState

    var optionsFieldOpen: Boolean = _quizPickOptions.isOptionsSectionVisible
    var questionCount: Int = _quizPickOptions.count
    var questionTopics: List<String> = _quizPickOptions.topics
    var unanswered: Boolean = _quizPickOptions.unanswered
    var wrongAnswers: Boolean = _quizPickOptions.wrongAnswersState
    var answerTime: Boolean = _quizPickOptions.answerTime

    init {

    }

    fun onEvent(event: TestPickEvent) {
        when (event) {
            is TestPickEvent.StartQuiz -> {
                CoroutineScope(Dispatchers.IO).launch {
//                    val questions: List<Question> = quizUseCase.startTest.invoke() //Start with 3 questions

                }
            }
            is TestPickEvent.OpenOptions -> {
                optionsFieldOpen = if (optionsFieldOpen == _quizPickOptions.isOptionsSectionVisible ){
                    !_quizPickOptions.isOptionsSectionVisible
                } else {
                    _quizPickOptions.isOptionsSectionVisible
                }
                }
            is TestPickEvent.ChooseCount -> {
                questionCount = _quizPickOptions.count
            }
            is TestPickEvent.PickUnanswered -> {

            }
            is TestPickEvent.PickWrongAnswered -> {
                _quizPickOptionsState.value = quizPickOptions.value.copy(
//                    wrongAnswers = !wrongAnswers
//                    wrongAnswers =
                )

            }
            is TestPickEvent.PickTime -> {
                //TODO create calculation for average and last time answers - give higher rating for last run 0.65?.

            }
            is TestPickEvent.ChooseTopics -> {
                _quizPickOptionsState.value = quizPickOptions.value.copy(
                    topics = questionTopics
                )
            }

        }
    }


}