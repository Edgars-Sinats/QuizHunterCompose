package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.TestOptions
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.QuizUseCase
import com.example.quizhuntercompose.ui.NavigationKeys
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

const val VIEW_MODEL = "TestViewModel: "

@HiltViewModel
class TestViewModel @Inject constructor(
    private val questionRepository: @JvmSuppressWildcards QuestionRepository,
    private val quizUseCase: @JvmSuppressWildcards QuizUseCase,
    savedStateHandle: @JvmSuppressWildcards SavedStateHandle,
//    private val quizUseCaseObj: @JvmSuppressWildcards String = "Start Test"
): ViewModel() {

    private val arguments = savedStateHandle.get<String>("testCategoryName")
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val jsonAdapter = moshi.adapter(TestOptions::class.java)!! //.lenient()
    private val testOptionsObject = jsonAdapter.fromJson(arguments!!)

//    private val arguments2: Int = checkNotNull(savedStateHandle.get<Int>("test1"))
//    private val arguments3: Boolean = checkNotNull(savedStateHandle.get<Boolean>("nonAns"))
//    private val arguments4: Boolean = checkNotNull(savedStateHandle.get<Boolean>("wrongAns"))


    private val _uiState = mutableStateOf<TestState>( TestState(showPreview = false) )      //
    val uiState: State<TestState> = _uiState

    private val _currentlySelectAnswer = mutableStateOf<Int?>(null)
    val currentlySelectAnswer: State<Int?> = _currentlySelectAnswer

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

//    private val _uiState = MutableStateFlow(TestState())
//    val uiState: StateFlow<TestState> = _uiState.asStateFlow()


    private var start: Long = 0
//    private var currentQuestionIndex = _uiState.value.currentQuestionIndex

    private var questionStateList: List<QuestionState>
    private var answerStateList: List<Answer>
    private var questionCount: Int
    private var questionCountForInit: Int = 0

    init {
        _isLoading.value = true

        questionStateList = emptyList()
        answerStateList = emptyList()
        questionCount = testOptionsObject!!.count

        viewModelScope.launch(Dispatchers.IO) {

            val listOfQuestion: List<Question> =  questionRepository.getMyQuestions(count = testOptionsObject.count, ids = testOptionsObject.ids, nonAns = testOptionsObject.nonAns, wrongAns = testOptionsObject.wrongAns)

            if (listOfQuestion != null) {
                Log.i("TestViewMod_List: ", "listOfQuestionConsist is not null!")

                listOfQuestion.forEach { question ->

                    val newQuestionState: QuestionState = QuestionState(question, chosenAnswer = null, questionStateId = questionCountForInit)
                    val newAnswerState: Answer = Answer(questionId = questionCountForInit, chosenAnswer = null, timeSpent = null, isFirstQuestion = questionCountForInit==0, isLastQuestion = questionCountForInit == listOfQuestion.lastIndex )
                    answerStateList = answerStateList + newAnswerState
                    questionStateList = questionStateList + newQuestionState
                    questionCountForInit++
                }

                delay(100)
                Log.i("TestViewMod: ", "Loading UI STATE")
                _uiState.value = TestState(
                    questionStateList,
                    answerStateList,
                    answerTime = false,
                    showPreview = false,
                    currentQuestionIndex = 0,
                    wrongAnswerCount = 0,
                    correctAnswerCount = 0
                )

            } else {
                Log.i(VIEW_MODEL, "Warning _uiState of question list failed!")
            }
            Log.i(VIEW_MODEL, "Created _uiState of new question/s")
            _isLoading.value = false
        }

    }

    fun onNewQuestionOpen(){
        start = System.currentTimeMillis() //TODO move in open new Question On display
    }

    fun onEvent(event: TestEvent, ) {
        //TODO check start time if answer already has been viewer using Skip. Don`t update time if answer has been answered(like using Previous)
        val currentQuestIndex = _uiState.value.currentQuestionIndex

        val endTime =  (System.currentTimeMillis() - start ).toInt()
        println("PRINTING End time: ${endTime} \n AND start: ${start}")
        println("Current time in Millis: ${System.currentTimeMillis()}")

        when (event) {

            is TestEvent.ShowDialog -> {
                println("PRINTING ")
                _uiState.value.run { _uiState.value= _uiState.value.copy(showDialog = !_uiState.value.showDialog) }
            }

            is TestEvent.Previous -> {

                updateTime(currentQuestIndex, endTime) //call update
                _uiState.value = _uiState.value.copy(currentQuestionIndex = currentQuestIndex.dec() )
                _currentlySelectAnswer.value = _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer
            }

            //TODO If onAppDestroy, should save currentQuestionState.
            is TestEvent.FinishTestScreen -> {
            }
            is TestEvent.Submit -> {

                Log.i("TestViewModel Submit", "showPreview:" + _uiState.value.showPreview)
                Log.i("TestViewModel Submit", "currentQuestIndex: $currentQuestIndex. And " )

                updateAnswer(answerNbr = event.value, currentQuestIndex = currentQuestIndex, endTime)

                // Check if all answers is updated, except current. currentQuestIndex is not jet updated, and checkIfAllAnswered() read it as non answered before updateAnswer() is finished.
                if ( checkIfAllAnswered(uiState.value.questionStateList, currentQuestIndex) )  {
                    Log.i("TestViewModel Submit", "all answer checked")
                    updateQuestion() //update In Database // TODO check how to save answers when app is onDestroy(), at least answers what are answered in the test.
                } else {
                    Log.i("TestViewModel Submit", "all answer checked, taking next question")

                    if (_uiState.value.answers[currentQuestIndex].isLastQuestion) {
                        Log.i("TestViewModel Submit", "all answers should be checked, last answer...")
                        Log.i("TestViewModel Submit", " _uiState.value.answers[currentQuestIndex]... ${_uiState.value.answers[currentQuestIndex]} ")

                        val unansweredQuestId = _uiState.value.questionStateList.first { questionState -> questionState.chosenAnswer == null }.questionStateId
                        _uiState.value = uiState.value.copy(currentQuestionIndex = unansweredQuestId)
                        _currentlySelectAnswer.value = unansweredQuestId //TODO check new channges
                        Log.i("TestViewModel Non ans", "Non answered question: $unansweredQuestId" )

                    } else {
                        _uiState.value = uiState.value.copy( currentQuestionIndex = _uiState.value.currentQuestionIndex +1 ) //Show next question!
                        _currentlySelectAnswer.value = _uiState.value.questionStateList[_uiState.value.currentQuestionIndex].chosenAnswer
                        println("PRINTING 2 next question ans: ${uiState.value.answers} \n AND index of question: ${uiState.value.currentQuestionIndex}")
                    }

                }
            }

            is TestEvent.Next -> {

                Log.i("TestViewModel Next: ", "answer1: " + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString())
                updateTime(currentQuestIndex, endTime)
                _uiState.value = _uiState.value.copy( currentQuestionIndex = _uiState.value.currentQuestionIndex.inc() )
                _currentlySelectAnswer.value = _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer

                Log.i("TestViewModel Next: ", "Next has executed:___\n Chosen (A) answer: " + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString() + "\n currentQIndex: " + _uiState.value.currentQuestionIndex)
            }

            is TestEvent.AnswerSelected -> {
                println("PRINTING AnswerSelected: ${uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.value.currentQuestionIndex}")
                println("PRINTING Event value:: ${event.value}")
                _isLoading.value = true


//                _uiState.update { newState ->
//                    uiState.value.answers[uiState.value.currentQuestionIndex].copy(chosenAnswer = event.value)
//                    newState.copy(answers = uiState.value.answers)
////                currentState.answers[_uiState.value.currentQuestionIndex].copy(chosenAnswer = selectedAnswer)
////                (uiState.value.copy(answers = answer1)  )
//            }

//                _uiState.update { it.copy(
//                answers =
//                )}
//                _uiState.apply {
//                _uiState.value.answers.get(_uiState.value.currentQuestionIndex) =   _uiState.value.answers.get(index = _uiState.value.currentQuestionIndex).copy(chosenAnswer = 2)
                _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer = event.value
                _currentlySelectAnswer.value = event.value // From MC STAR - how can make event.id

                //chosenAnswer = event.value
//                _uiState.apply { _uiState}
//                _uiState.value.apply {  }
//                _currentlySelectAnswer.value = event.value
//                _uiState.value.answers[_uiState.value.currentQuestionIndex].copy(chosenAnswer = event.value)

//                _uiState
                _isLoading.value = false
                println("PRINTING AnswerSelected: ${uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.value.currentQuestionIndex}")
                println("PRINTING ViewModel: ${_uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer} \n AND _isLoading: ${_isLoading.value}")

            }
        }
    }

    /**
     *  ON FINAL QUESTION - update questions
     * Update questions in db. Iterating throw question state.
     * TODO - Might been good idea to put answer time in question state instead of answer.
     */
    private fun updateQuestion() = CoroutineScope(Dispatchers.IO).launch{
        var _questionStatePosition: Int
        var _correctAnswer: Int = 0
        _questionStatePosition= 0

        _uiState.value.questionStateList.forEach {
            val _currentQuestionState = _uiState.value.questionStateList[_questionStatePosition]
            val _currentQuestion = _currentQuestionState.question
            val answerTimes = _currentQuestion.correctAnswers + _currentQuestion.nonAnswers + _currentQuestion.nonAnswers
            var lastAnsTime = _uiState.value.answers[_questionStatePosition].timeSpent  //AnswerTime // lastAnswerTime
            val _averageAnsTime = _currentQuestion.averageAnswerTime
            val _totalAnsTime = _averageAnsTime * answerTimes

            if (_currentQuestionState.chosenAnswer == _currentQuestion.correctAnswer-1){
                _correctAnswer += 1
            }


            if (lastAnsTime == null) {
                Log.i("ViewMod. LastAns time:", "Last ans time can`t be null, if question is not answered 0 or 0.1 sec need to add to db.: " + _uiState.value.answers[_questionStatePosition].timeSpent.toString() )
                lastAnsTime = 1
            }

            try {
                questionRepository.updateQuestion(
                    _uiState.value.questionStateList[_questionStatePosition].question.copy(
                        lastAnswerTime = lastAnsTime,
                        correctAnswers =
                        if (_currentQuestionState.chosenAnswer == _currentQuestion.correctAnswer-1) {
                            it.question.correctAnswers+1
                        } else {
                            it.question.correctAnswers
                        },

                        nonAnswers =
                        if (lastAnsTime == 1 && (_currentQuestionState.chosenAnswer != _currentQuestion.correctAnswer-1)) {
                            it.question.nonAnswers+1
                        }else  {
                            it.question.nonAnswers
                        },
                        wrongAnswers =
                        if (_currentQuestionState.chosenAnswer != _currentQuestion.correctAnswer-1) {
                            it.question.wrongAnswers+1
                        }else {
                            it.question.wrongAnswers
                        },
                        averageAnswerTime = ( _totalAnsTime + lastAnsTime ) / ( answerTimes+1 ), //TODO I might want to change last answer time value more valuable once more questions has been answered.
                        // So the average ans time change with bigger impact when multiple answers already answered.
                        // For example when will choose in testChoose screen, based on answer time.,
                        // if some question has been answered multiple times with long ans time, and last time it was answered quickly,
                        // it show person know the answer finally well, and should be skipped or ranked lower for choosing question.
                    )//_uiState
                )//updateQuestion
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            }
            Log.i("TestViewModel Update: ", "NextQuestion to be loaded in db. Current loaded: " + _questionStatePosition )
            _questionStatePosition += 1
        }
//        _uiState.value.copy(showPreview = true) //SHOW PREVIEW, WRONG - Not applying value!
        _uiState.value = _uiState.value.copy(
            correctAnswerCount = _correctAnswer,
            wrongAnswerCount = _uiState.value.questionStateList.size - _correctAnswer,
            showPreview = true,
            showDialog = true
        )
    }

    /**
     * Check if all question has been answered.
     * If at least one
     * @param questionState.chosenAnswer has null value - one question has not been answered
     * Return @param true if all questions has been answered, false otherwise.
     *
     */

    //TODO something is off, even when middle is not answered, show all good.
    private fun checkIfAllAnswered(questionList: List<QuestionState>, lastQuestionId: Int) : Boolean{
        //if NotNull(QuestSFound) -> no ans(for one).       // null -> non answered
        return questionList.find {
                questionState ->
                    questionState.chosenAnswer == null      //Some unanswered
        } == null //All answered
//                &&
//                questionState.questionStateId != lastQuestionId
    }


    private fun updateTime(currentQuestIndex: Int,timeOnQuestion: Int){
        val answerList: List<Answer> = _uiState.value.answers
        answerList[currentQuestIndex].apply {
            timeSpent = timeSpent?.plus(timeOnQuestion)
        }
        _uiState.value.copy(answers = answerList)

//        _uiState.update {
//            val answerList: List<Answer> = _uiState.value.answers
//            answerList[currentQuestIndex].apply {
//                timeSpent = timeSpent?.plus(timeOnQuestion)
//            }
//            it.copy(answers = answerList)
//        }
    }

    private fun updateAnswer(answerNbr: Int, currentQuestIndex: Int, timeOnQuestion: Int){

        val answerList: List<Answer> = _uiState.value.answers
        val questionList: List<QuestionState> = _uiState.value.questionStateList
        answerList.elementAt(currentQuestIndex).apply {
            chosenAnswer = answerNbr
            timeSpent = timeSpent?.plus(timeOnQuestion)
        }
        questionList.elementAt(currentQuestIndex).apply {
            chosenAnswer = answerNbr

//            if (questionState1.questionStateId == questionList[currentQuestIndex].questionStateId )
//                questionState1.copy(chosenAnswer = answerNbr)
//            else questionState1
        }

        _uiState.value = _uiState.value.copy(
            answers = answerList,
            questionStateList = questionList,
        )
    }
}


