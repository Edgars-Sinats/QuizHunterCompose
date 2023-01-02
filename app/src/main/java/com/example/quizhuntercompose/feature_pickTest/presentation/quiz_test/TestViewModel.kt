package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
import com.example.quizhuntercompose.feature_pickTest.db.repository.QuestionRepositoryImpl
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.GetStartTest
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.QuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TestViewModel @Inject constructor(
    private val questionRepository: @JvmSuppressWildcards QuestionRepository,
    private val quizUseCase: @JvmSuppressWildcards QuizUseCase,
//    savedStateHandle: @JvmSuppressWildcards SavedStateHandle,
//    private val quizUseCaseObj: @JvmSuppressWildcards String = "Start Test"
): ViewModel() {

    private val _uiState = mutableStateOf<TestState>( TestState() )        //
    val uiState: State<TestState> = _uiState

    private val _currentlySelectAnswer = mutableStateOf<Int?>(null)
    val currentlySelectAnswer: State<Int?> = _currentlySelectAnswer

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

//    private val _uiState = MutableStateFlow(TestState())
//    val uiState: StateFlow<TestState> = _uiState.asStateFlow()


    private var start: Long = 0
    private var currentQuestionIndex = _uiState.value.currentQuestionIndex

    private var questionStateList: List<QuestionState>
    private var answerStateList: List<Answer>
    private var questionCount: Int

    init {
        _isLoading.value = true

        questionStateList = emptyList()
        answerStateList = emptyList()
        questionCount = 0

        viewModelScope.launch(Dispatchers.IO) {

//            if (quizUseCaseObj == "Start test"){
//                true //TODO need to check which useCase provided.
//            }
            val listOfQuestion: List<Question?>?= questionRepository.getXQuestions(3)

            if (listOfQuestion != null) {
                listOfQuestion.forEach { question ->

                    if (question != null) {
                        val newQuestionState: QuestionState = QuestionState(question, chosenAnswer = null, questionStateId = questionCount)
                        val newAnswerState: Answer = Answer(questionId = questionCount, chosenAnswer = null, timeSpent = null, isFirstQuestion = questionCount==0, isLastQuestion = questionCount == listOfQuestion.lastIndex )
                        answerStateList = answerStateList + newAnswerState
                        questionStateList = questionStateList + newQuestionState
                        questionCount++
                    } else {
                        Log.i("TestViewMod: ", "listOfQuestionConsist of null question... Please investigate")
                    }
                }

                _uiState.value = TestState(
                    questionStateList,// stucked here
                    answerStateList,
                    answerTime = false,
                    showPreview = false,
                    currentQuestionIndex = 0
                )

                _isLoading.value = false
//                _uiState.update { it }

            } else {
                Log.i("TestViewMod: ", "Warning _uiState of question list failed!!!")
            }
            Log.i("TestViewMod: ", "Created _uiState of 3 question") //TODO 3/question...
        }

    }

    fun onAnswerSelected(selectedAnswer: Int){
//        JatpackComposeState instead of flow.
        _isLoading.value = true


        Log.i("TestViewModel ChosAns: ", "_uiState BEFORE UPDATE " + _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer.toString())

        Log.i("TestViewModel ChosAns: ", "uiState answer" + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString())
//        Log.i("TestViewModel ChosAns: ", "answer1:___ " + answer1[uiState.value.currentQuestionIndex].chosenAnswer.toString())
//        viewModelScope.launch {
//        viewModelScope.launch {

//        answerStateList = uiState.value.answers[1].copy(chosenAnswer = selectedAnswer)

//            _uiState.update {
//                it.copy(answers = answerStateList)
////                currentState.answers[currentState.currentQuestionIndex].copy(chosenAnswer = selectedAnswer)
////                currentState.answers[_uiState.value.currentQuestionIndex].copy(chosenAnswer = selectedAnswer)
////                (uiState.value.copy(answers = answer1)  )
//            }
//            _uiState.update { it.copy(
//                answers = answer1
//            )}
//        }

        Log.i("TestViewModel ChosAns: ", "_uiState AFTER UPDATE " + _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer.toString())
        _isLoading.value = false
//        }
    }

    fun onNewQuestionOpen(){
         start = System.currentTimeMillis() //TODO move in open new Question On display
    }

    fun submitAnswer(answer: Int){
        val endTime =  (System.currentTimeMillis() - start ).toInt()
        updateAnswer(answer, _uiState.value.currentQuestionIndex, endTime)
        if (checkIfAllAnswered(uiState.value.questionStateList)) {
            updateQuestion()
        }
    }

    fun onEvent(event: TestEvent, ) {
        //TODO check start time if answer already has been viewer using Skip. Don`t update time if answer has been answered(like using Previous)
        val latestState = _uiState.value  //Not needed
        val _currentQuestionIndex = _uiState.value.currentQuestionIndex

        val endTime =  (System.currentTimeMillis() - start ).toInt()

        when (event) {

            is TestEvent.Previous -> {


                updateTime(_currentQuestionIndex, endTime) //call update
                _uiState.value = _uiState.value.copy(currentQuestionIndex = _currentQuestionIndex.dec() )
//                _uiState.update {
//                    it
//                }
            }

            //TODO If onAppDestroy, should save currentQuestionState.
            is TestEvent.Submit -> {

                updateAnswer(event.value, _currentQuestionIndex, endTime)

                if (checkIfAllAnswered(uiState.value.questionStateList)) {
                    updateQuestion() //update In Database // TODO check how to save answers when app is onDestroy(), at least answers what are answered in the test.
                } else {
                    _uiState.value = uiState.value.copy( currentQuestionIndex = _uiState.value.currentQuestionIndex +1 ) //Show next question! //TODO, check if next question in answered or any other in the row, otherwise open/check previous question from list.
                    _currentlySelectAnswer.value = null // From MC STAR - how can make event.id
                    println("PRINTING 2 ${uiState.value.answers} AND ${uiState.value.currentQuestionIndex}")
                }
            }

            is TestEvent.Next -> {
                Log.i("TestViewModel ChosAns: ", "answer1:___ " + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString())

                _uiState.value.currentQuestionIndex.inc()
                updateTime(_currentQuestionIndex, endTime)
                Log.i("TestViewModel ChosAns: ", "Next has executed:___ " + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString())
            }

            is TestEvent.AnswerSelected -> {
                _isLoading.value = true

                _currentlySelectAnswer.value = event.value // From MC STAR - how can make event.id
                _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer = event.value

                _isLoading.value = false

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
        _uiState.value.questionStateList.forEach {
            _questionStatePosition= 0
            val _currentQuestionState = _uiState.value.questionStateList[_questionStatePosition]
            val _currentQuestion = _currentQuestionState.question
            val answerTimes = _currentQuestion.correctAnswers + _currentQuestion.nonAnswers + _currentQuestion.nonAnswers
            var lastAnsTime = _uiState.value.answers[_questionStatePosition].timeSpent  //AnswerTime // lastAnswerTime
            val _averageAnsTime = _currentQuestion.averageAnswerTime
            val _totalAnsTime = _averageAnsTime * answerTimes

            if (lastAnsTime == null) {
                Log.i("ViewMod. LastAns time:", "Last ans time can`t be null, if question is not answered 0 or 0.1 sec need to add to db.: " + _uiState.value.answers[_questionStatePosition].timeSpent.toString() )
                lastAnsTime = 1
            }

            questionRepository.updateQuestion(
                _uiState.value.questionStateList[_questionStatePosition].question.copy(
                    lastAnswerTime = lastAnsTime,
                    correctAnswers =
                    if (_currentQuestionState.chosenAnswer == _currentQuestion.correctAnswer) {
                        it.question.correctAnswers+1
                    } else {
                        it.question.correctAnswers
                    },

                    nonAnswers =
                    if (lastAnsTime == 1 && (_currentQuestionState.chosenAnswer != _currentQuestion.correctAnswer)) {
                        it.question.nonAnswers+1
                    }else  {
                        it.question.nonAnswers
                    },
                    wrongAnswers =
                    if (_currentQuestionState.chosenAnswer != _currentQuestion.correctAnswer) {
                        it.question.wrongAnswers+1
                    }else {
                        it.question.wrongAnswers
                    },
                    averageAnswerTime = ( _totalAnsTime + lastAnsTime ) / ( answerTimes+1 ), //TODO I might want to change last answer time value more valuable once more questions has been answered.
                                                                    // So the average ans time change with bigger impact when multiple answers already answered.
                                                                    // For example when will choose in testChoose screen, based on answer time.,
                                                                    // if some question has been answered multiple times with long ans time, and last time it was answered quickly,
                                                                    // it show person know the answer finally well, and should be skipped or ranked lower for choosing question.
                )
            )
            _questionStatePosition++
        }
        _uiState.value.copy(showPreview = true)

    }

    /**
     * Check if all question has been answered.
     * If at least one
     * @param questionState.chosenAnswer has null value - one question has not been answered
     * Return @param true if all questions has been answered, false otherwise.
     *
     */

    private fun checkIfAllAnswered(questionList: List<QuestionState>) : Boolean{
        //if NotNull(QuestSFound) -> no ans(for one).       // null -> non answered
        return questionList.find { questionState -> questionState.chosenAnswer == null } == null
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
        questionList.map {
                questionState1 ->
            if (questionState1.questionStateId == questionList[currentQuestIndex].questionStateId )
                questionState1.copy(chosenAnswer = uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer)
            else questionState1
        }

        _uiState.value = _uiState.value.copy(
            answers = answerList,
            questionStateList = questionList,
        )
    }
}