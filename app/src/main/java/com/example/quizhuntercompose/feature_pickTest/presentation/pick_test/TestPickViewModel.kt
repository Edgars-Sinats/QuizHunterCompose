package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
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
//    private lateinit var screenArguments:Int
    var thisTestId: Int = 0
    private val _quizPickOptions = mutableStateOf(TestPickOptionsState( questions = emptyList(), pickedTopicId = emptyList(), isOptionsSectionVisible = false, pickedTestId = 0 ))
    val uiState: State<TestPickOptionsState> = _quizPickOptions
    var topicNames: List<Topic> = emptyList()

//    private val _quizPickOptionsState = mutableStateOf(TestPickOptionsState())
//    var quizPickOptions: State<TestPickOptionsState> = _quizPickOptionsState
//    var optionsFieldOpen: Boolean = _quizPickOptions.value.isOptionsSectionVisible

    var questionCount: Int = _quizPickOptions.value.totalCount
    var questionStateList: List<Question> = _quizPickOptions.value.questions
    var databaseLoading : Boolean = true
    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
    fun getTestId(testId: Int){
        thisTestId = testId
    }
    fun loadTopicsTests(){
        Log.i(TAG, "Init func")
        val listOfIds : MutableList<Int>  = mutableListOf()

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            Log.i(TAG, ".launch 1.")
            async {
                topicNames = questionRepository.getAllTopics(thisTestId)
                Log.i(TAG, ".launch async 1. topic names: $topicNames.")
            }.await()

            //- 1
            async {
                topicNames.forEach {
                    listOfIds.add((it.topicId) )
                }
                Log.i(TAG, ".launch async 2. listOfIds: $listOfIds.")
            }.await()

            async { questionCount = questionRepository.getQuestionCountChecker(
                listOfIds,
                testId = thisTestId,
                nonAns = false,
                wrongAns = false )
            }.await()

            try {
                withContext(Dispatchers.Main){
                    _quizPickOptions.value = TestPickOptionsState(
                        topics = topicNames, //TODO see all topics
                        totalCount = questionCount,
                        pickedTopicId = listOfIds.toList(),
                        isOptionsSectionVisible = false,
                        count = questionCount / 2,
                        pickedTestId = thisTestId
                    )
                    databaseLoading = false
                }


            } catch (cancellationException: CancellationException) {
                throw cancellationException
            }
        }
        Log.i(TAG, "questionCount: $questionCount")
    }

//    init {
//    }



    private fun checkCountVsTotal(){

        if (_quizPickOptions.value.totalCount == _quizPickOptions.value.count) {
            //TODO add some
            _quizPickOptions.value = _quizPickOptions.value

        } else if( _quizPickOptions.value.totalCount < _quizPickOptions.value.count) {
            //Update UI

            _quizPickOptions.value = _quizPickOptions.value.copy(count = _quizPickOptions.value.totalCount)
//            return
        }else{
            //IF total count > then count, & count is 0, automatically increase count to 1.
            if (_quizPickOptions.value.count == 0 ){
                //TODO add user preferences( DataStore [https://developer.android.com/topic/libraries/architecture/datastore]) as minimal count.
                _quizPickOptions.value = _quizPickOptions.value.copy(count = _quizPickOptions.value.totalCount/2)
            }else {
                _quizPickOptions.value = _quizPickOptions.value.copy(count = _quizPickOptions.value.totalCount/2)
            }
        }
    }

    fun onEvent(event: TestPickEvent) {
        when (event) {
            is TestPickEvent.StartQuiz -> {
                //TODO navigate/call TestScreen
//                CoroutineScope(Dispatchers.IO).launch {
//                    event.value
////                    val questions: List<Question> = quizUseCase.startTest.invoke() //Start with 3 questions
//                }
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
                            totalCount = questionRepository.getQuestionCountChecker(_quizPickOptions.value.pickedTopicId, testId = thisTestId, !_quizPickOptions.value.unanswered, wrongAns = false),
                            unanswered = !_quizPickOptions.value.unanswered,
                            wrongAnswersState = false)
                    } else {
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountChecker(_quizPickOptions.value.pickedTopicId, testId = thisTestId, !_quizPickOptions.value.unanswered, wrongAns = true),
                            unanswered = !_quizPickOptions.value.unanswered)
                    }
                    checkCountVsTotal()
                }

            }

            is TestPickEvent.PickWrongAnswered -> {
                CoroutineScope(Dispatchers.IO).launch {

                    if (_quizPickOptions.value.unanswered){

                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountChecker(_quizPickOptions.value.pickedTopicId, testId = thisTestId, false, wrongAns = !_quizPickOptions.value.wrongAnswersState),
                            wrongAnswersState = !_quizPickOptions.value.wrongAnswersState,
                            unanswered = false
                        )
                    } else {
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountChecker(_quizPickOptions.value.pickedTopicId, testId = thisTestId, true, wrongAns = !_quizPickOptions.value.wrongAnswersState),
                            wrongAnswersState = !_quizPickOptions.value.wrongAnswersState,
                            unanswered = true //TODO why didn`t implement?
                        )
                    }

                    checkCountVsTotal()
                }
            }

            //TODO
            is TestPickEvent.PickTime -> {
                //TODO create calculation for average and last time answers - give higher rating for last run 0.65?.

                _quizPickOptions.value = _quizPickOptions.value.copy(
                    answerTime = !_quizPickOptions.value.answerTime
                )
            }

            //Usless
//            is TestPickEvent.CheckTopicQuestionCount -> {
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        questionCount = questionRepository.getQuestionCount(event.topic)
//                    } catch (cancellationException: CancellationException) {
//                        throw cancellationException
//                    }
//                    Log.i("TestPickViewModel", "try success, new question count: $questionCount"  )
//                    _quizPickOptions.value = _quizPickOptions.value.copy(totalCount = questionCount)
//
//                }
////                return count
//            }
//
//            //Usless
//            is TestPickEvent.CheckTopicsQuestionsCount -> {
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        questionCount = questionRepository.getQuestionCountChecker(event.topic, _quizPickOptions.value.unanswered, wrongAns = _quizPickOptions.value.wrongAnswersState)
//                    } catch (cancellationException: CancellationException) {
//                        throw cancellationException
//                    }
//                    _quizPickOptions.value = _quizPickOptions.value.copy(totalCount = questionCount)
//
//                }
//            }
            //Good
            is TestPickEvent.CheckAllTopics -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val listOfIds : MutableList<Int>  = mutableListOf()

                    //-1 removed
                    topicNames.forEach {
                        listOfIds.add( it.topicId )
                    }

                    if ( _quizPickOptions.value.pickedTopicId == listOfIds.toList() ) {
//                        questionCount = questionRepository.getQuestionCount(0)
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = 0,
                            pickedTopicId = emptyList()
                        )


                    } else {
                        _quizPickOptions.value = _quizPickOptions.value.copy(
                            totalCount = questionRepository.getQuestionCountChecker(listOfIds.toList(), testId = thisTestId, _quizPickOptions.value.unanswered, wrongAns = _quizPickOptions.value.wrongAnswersState),
                            pickedTopicId = listOfIds.toList()
                        )
                    }
                    checkCountVsTotal()
                }
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
                        totalCount = questionRepository.getQuestionCountChecker( topicList.toList(), testId = thisTestId, _quizPickOptions.value.unanswered, wrongAns = _quizPickOptions.value.wrongAnswersState ),
                        pickedTopicId = topicList.toList()
                    )
                    checkCountVsTotal()
                }
                Log.i("TestPickView", "Total questions: ${_quizPickOptions.value.totalCount} and chosen Questions: ${_quizPickOptions.value.count}" )
            }

            is TestPickEvent.UploadTestQuestions -> {
                CoroutineScope(Dispatchers.IO).launch {

                }
            }

            else -> {}
        }//event
    }//onEvent

}
