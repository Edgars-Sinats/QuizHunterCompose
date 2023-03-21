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
    private val questionRepository: @JvmSuppressWildcards QuestionRepository,
//    private val quizUseCase: @JvmSuppressWildcards QuizUseCase,

//    onNavigationRequested: @JvmSuppressWildcards (itemId: String) -> Unit
) : ViewModel() {
    private val TAG = "TestPickViewModel"
    //For now, replace with barPicker
    private val _quizPickOptions = mutableStateOf(TestPickOptionsState( questions = emptyList(), pickedTopicId = emptyList(), isOptionsSectionVisible = false ))
    val uiState: State<TestPickOptionsState> = _quizPickOptions
    var topicNames: List<Topic> = emptyList()

//    private val _quizPickOptionsState = mutableStateOf(TestPickOptionsState())
//    var quizPickOptions: State<TestPickOptionsState> = _quizPickOptionsState
//    var optionsFieldOpen: Boolean = _quizPickOptions.value.isOptionsSectionVisible

    var questionCount: Int = _quizPickOptions.value.totalCount
    var questionStateList: List<Question> = _quizPickOptions.value.questions
    var databaseLoading : Boolean = true

    init {


        viewModelScope.launch(Dispatchers.IO) {
//            Log.i(TAG, "viewModel_Start")

            val exampleTopicNames: List<Topic> = questionRepository.getAllTopics()
            delay(400)
            topicNames = exampleTopicNames

            val listOfIds : MutableList<Int>  = mutableListOf()
//            Log.i(TAG, "topicNames: $topicNames")

            topicNames.forEach { listOfIds.add( it.topicId -1 ) }
//            Log.i(TAG, "listOfIds: $listOfIds")

            questionCount = questionRepository.getQuestionCountChecker(listOfIds,
                nonAns = false,
                wrongAns = false
            )
//            Log.i(TAG, "questionCount: $questionCount")
//            Log.i(TAG, "topicNames: $topicNames")

//            val listOfQuestion: List<Question?> = questionRepository.getXQuestions(questionCount)

            delay(1000) //TODO How to wait till loaded before creating State

                    try {
//                        questionStateList = listOfQuestion as List<Question>

                        _quizPickOptions.value = TestPickOptionsState(
                            topics = topicNames, //TODO see all topics
                            totalCount = questionCount,
                            pickedTopicId = listOfIds.toList(),
//                            pickedQuestions = questionStateList,
                            isOptionsSectionVisible = false,
                            count = questionCount/2
                        )

                    } catch (cancellationException: CancellationException) {
                        throw cancellationException
                    }
            Log.i(TAG, "questionCount: $questionCount")



        }
        databaseLoading = false
    }

    private fun checkCountVsTotal(){
        if (_quizPickOptions.value.totalCount < _quizPickOptions.value.count) {
            _quizPickOptions.value = _quizPickOptions.value.copy(count = _quizPickOptions.value.totalCount)
        } else {
            _quizPickOptions.value = _quizPickOptions.value.copy(count = _quizPickOptions.value.count)
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
                Log.i(TAG, "ChooseCount")
                _quizPickOptions.value = _quizPickOptions.value.copy(count = event.value)
//                questionCount = _quizPickOptions.value.count
            }

            is TestPickEvent.OpenOptions -> {
//                optionsFieldOpen = !optionsFieldOpen
                _quizPickOptions.value = _quizPickOptions.value.copy(isOptionsSectionVisible = !_quizPickOptions.value.isOptionsSectionVisible)
            }

            is TestPickEvent.PickUnanswered -> {
                CoroutineScope(Dispatchers.IO).launch{

                    if (_quizPickOptions.value.wrongAnswersState){
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountChecker(_quizPickOptions.value.pickedTopicId, !_quizPickOptions.value.unanswered, wrongAns = false),
                            unanswered = !_quizPickOptions.value.unanswered,
                            wrongAnswersState = false)
                    } else {
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountChecker(_quizPickOptions.value.pickedTopicId, !_quizPickOptions.value.unanswered, wrongAns = true),
                            unanswered = !_quizPickOptions.value.unanswered)
                    }

                }
                checkCountVsTotal()
            }

            is TestPickEvent.PickWrongAnswered -> {
                CoroutineScope(Dispatchers.IO).launch {

                    if (_quizPickOptions.value.unanswered){

                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountChecker(_quizPickOptions.value.pickedTopicId, false, wrongAns = !_quizPickOptions.value.wrongAnswersState),
                            wrongAnswersState = !_quizPickOptions.value.wrongAnswersState,
                            unanswered = false
                        )
                    } else {
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountChecker(_quizPickOptions.value.pickedTopicId, true, wrongAns = !_quizPickOptions.value.wrongAnswersState),
                            wrongAnswersState = !_quizPickOptions.value.wrongAnswersState
                        )
                    }

                }
                checkCountVsTotal()
            }

            //TODO
            is TestPickEvent.PickTime -> {
                //TODO create calculation for average and last time answers - give higher rating for last run 0.65?.
                _quizPickOptions.value = _quizPickOptions.value.copy(
                    answerTime = !_quizPickOptions.value.answerTime
                )
            }

            //Usless
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

            //Usless
            is TestPickEvent.CheckTopicsQuestionsCount -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        questionCount = questionRepository.getQuestionCountChecker(event.topic, _quizPickOptions.value.unanswered, wrongAns = _quizPickOptions.value.wrongAnswersState)
                    } catch (cancellationException: CancellationException) {
                        throw cancellationException
                    }
                    _quizPickOptions.value = _quizPickOptions.value.copy(totalCount = questionCount)

                }
            }
            //Good
            is TestPickEvent.CheckAllTopics -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val listOfIds : MutableList<Int>  = mutableListOf()

                    topicNames.forEach { listOfIds.add( it.topicId-1 ) }

                    if ( _quizPickOptions.value.pickedTopicId == listOfIds.toList() ) {
//                        questionCount = questionRepository.getQuestionCount(0)
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = 0,
                            pickedTopicId = emptyList()
                        )


                    } else {
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountChecker(listOfIds.toList(), _quizPickOptions.value.unanswered, wrongAns = _quizPickOptions.value.wrongAnswersState),
                            pickedTopicId = listOfIds.toList()
                        )
                    }
                }
                checkCountVsTotal()
                Log.i("TestPickView", "Total questions: ${_quizPickOptions.value.totalCount} and chosen Questions: ${_quizPickOptions.value.count}" )

            }
            //Good
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
                        totalCount = questionRepository.getQuestionCountChecker( topicList.toList(), _quizPickOptions.value.unanswered, wrongAns = _quizPickOptions.value.wrongAnswersState ),
                        pickedTopicId = topicList.toList()
                    )
                }
                checkCountVsTotal()
                Log.i("TestPickView", "Total questions: ${_quizPickOptions.value.totalCount} and chosen Questions: ${_quizPickOptions.value.count}" )
            }

        }//event
    }//onEvent

}