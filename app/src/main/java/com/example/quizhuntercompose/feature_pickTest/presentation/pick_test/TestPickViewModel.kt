package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.QuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class TestPickViewModel @Inject constructor(
//    private val quizUseCase: QuizUseCase,
    private val questionRepository: @JvmSuppressWildcards QuestionRepository,
    private val quizUseCase: @JvmSuppressWildcards QuizUseCase,

//    private val savedStateHandle: @JvmSuppressWildcards SavedStateHandle,
//    onNavigationRequested: @JvmSuppressWildcards (itemId: String) -> Unit
) : ViewModel() {

    //For now, replace with barPicker
    private val _quizPickOptions = mutableStateOf(TestPickOptionsState( questions = emptyList(), pickedQuestions = emptyList(), pickedTopicId = emptyList(), isOptionsSectionVisible = false ))  //It is 5 already inside
    val uiState: State<TestPickOptionsState> = _quizPickOptions
    var topicNames: List<Topic> = emptyList()
    var selectedTopic: List<Int> = emptyList()

//    private val _quizPickOptionsState = mutableStateOf(TestPickOptionsState())
//    var quizPickOptions: State<TestPickOptionsState> = _quizPickOptionsState

//    var optionsFieldOpen: Boolean = _quizPickOptions.value.isOptionsSectionVisible

    var questionCount: Int = _quizPickOptions.value.totalCount
    var questionStateList: List<Question> = _quizPickOptions.value.questions

    init {

        questionStateList = emptyList()
        questionCount = 10

        viewModelScope.launch(Dispatchers.IO) {

            val listOfQuestion: List<Question?> = questionRepository.getXQuestions(questionCount)
            val exampleTopicNames = questionRepository.getAllTopics()
            topicNames = exampleTopicNames

            delay(900) //TODO How to wait till loaded before creating State

            if (listOfQuestion != null){ //Might not need as try catch.

                    try {
                        questionStateList = listOfQuestion as List<Question>

                        _quizPickOptions.value = TestPickOptionsState(
                            topics = emptyList(), //TODO see all topics
                            totalCount = questionCount,
                            pickedTopicId = emptyList(),
                            pickedQuestions = questionStateList,
                            isOptionsSectionVisible = false,
                            count = questionCount/2
                        )

                    } catch (cancellationException: CancellationException) {
                        throw cancellationException
                    }
            }

        }
    }

    fun onEvent(event: TestPickEvent) {
        when (event) {
            is TestPickEvent.StartQuiz -> {
                //TODO navigate/call TestScreen
                CoroutineScope(Dispatchers.IO).launch {

//                    quizUseCase.
                    event.value
//                    val questions: List<Question> = quizUseCase.startTest.invoke() //Start with 3 questions
                }
            }
            is TestPickEvent.ChooseCount -> {
                Log.i("TestPickViewModel", "ChooseCount")
                _quizPickOptions.value = _quizPickOptions.value.copy(count = event.value)
//                questionCount = _quizPickOptions.value.count
            }
            is TestPickEvent.OpenOptions -> {
//                optionsFieldOpen = !optionsFieldOpen
                _quizPickOptions.value = _quizPickOptions.value.copy(isOptionsSectionVisible = !_quizPickOptions.value.isOptionsSectionVisible)
            }
            is TestPickEvent.PickUnanswered -> {
                _quizPickOptions.value = _quizPickOptions.value.copy(unanswered = !_quizPickOptions.value.unanswered)

            }
            is TestPickEvent.PickWrongAnswered -> {
                _quizPickOptions.value = _quizPickOptions.value.copy(
                    wrongAnswersState = !_quizPickOptions.value.wrongAnswersState
//                    wrongAnswers =
                )
            }
            is TestPickEvent.PickTime -> {
                //TODO create calculation for average and last time answers - give higher rating for last run 0.65?.
                _quizPickOptions.value = _quizPickOptions.value.copy(
                    answerTime = !_quizPickOptions.value.answerTime
                )
            }

            is TestPickEvent.CheckTopicQuestionCount -> {

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        questionCount = questionRepository.getQuestionCount(event.topic)
                    } catch (cancellationException: CancellationException) {
                        throw cancellationException
                    }
                    Log.i("TestPickViewModel", "try success, new question count: $questionCount"  )
                    _quizPickOptions.value = _quizPickOptions.value.copy(totalCount = questionCount)

                }
//                return count
            }

            is TestPickEvent.CheckTopicsQuestionCount -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        questionCount = questionRepository.getQuestionCountFrom(event.topic, event.noAns)
                    } catch (cancellationException: CancellationException) {
                        throw cancellationException
                    }
                    _quizPickOptions.value = _quizPickOptions.value.copy(totalCount = questionCount)

                }
            }
            is TestPickEvent.CheckAllTopics -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val listOfIds : MutableList<Int>  = mutableListOf()

                    topicNames.forEach { listOfIds.add( it.topicId ) }

                    if ( _quizPickOptions.value.pickedTopicId == listOfIds.toList() ) {
//                        questionCount = questionRepository.getQuestionCount(0)
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = 0,
                            pickedTopicId = emptyList()
                        )


                    } else {
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountFrom(listOfIds.toList(), 3),
                            pickedTopicId = listOfIds.toList()
                        )
                    }
                }

//                val topicIds = topicNames.forEach { it -> listOfIds.add(topicNames.topicId ) }
//                _quizPickOptions.value = _quizPickOptions.value.copy(pickedTopicId =  listOfIds)
            }
            is TestPickEvent.CheckTopics -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val topicList = _quizPickOptions.value.pickedTopicId.toMutableList()

//                    val listOfIds : MutableList<Int>  = mutableListOf()
//                    topicNames.forEach { listOfIds.add( it.topicId ) }

                    if (topicList.contains( event.topic ) ) {
                        topicList.remove(event.topic)

                    } else {
                        topicList.add(event.topic)
                    }
//                    _quizPickOptions.value = _quizPickOptions.value.copy(pickedTopicId = topicList.toList())
                    _quizPickOptions.value = _quizPickOptions.value.copy(
                        totalCount = questionRepository.getQuestionCountFrom(topicList.toList(), 3),
                        pickedTopicId = topicList.toList()
                    )
                }
            }

        }//event
    }//onEvent

}