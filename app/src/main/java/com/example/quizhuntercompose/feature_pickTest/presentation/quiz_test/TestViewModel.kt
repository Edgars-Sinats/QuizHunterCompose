package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test

//import androidx.compose.foundation.layout.fillMaxWidth
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.*
import com.example.quizhuntercompose.feature_pickTest.db.data_source.QuizDatabase
import com.example.quizhuntercompose.feature_pickTest.db.repository.QuestionRepositoryImpl
//import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.asStateFlow
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
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _uiState = MutableStateFlow(TestState())        //
    val uiState: StateFlow<TestState> = _uiState.asStateFlow()
//    get() = _uiState //.asStateFlow() //read-only public
//        get() = _uiState

    fun getState1(): StateFlow<TestState> = _uiState

    private lateinit var surveyInitialState: TestState
    private var start: Long = 0
    private var currentQuestionIndex = _uiState.value.currentQuestionIndex


    var questionStateList: List<QuestionState>
    var answerStateList: List<Answer>
    var questionCount: Int

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
                    showPreview = false
                )

                _isLoading.value = false
//                _uiState.update { it }

            } else {
                Log.i("TestViewMod: ", "Warning _uiState of question list failed!!!")
            }
            Log.i("TestViewMod: ", "Created _uiState of 3 question") //TODO 3/question...
        }

    }

    fun onAnswerSelected(answer1: List<Answer>){
        _isLoading.value = true
        Log.i("TestViewModel ChosAns: ", "_uiState BEFORE UPDATE " + _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer.toString())

        Log.i("TestViewModel ChosAns: ", "uiState answer" + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString())
        Log.i("TestViewModel ChosAns: ", "answer1:___ " + answer1[uiState.value.currentQuestionIndex].chosenAnswer.toString())
//        viewModelScope.launch {
//        viewModelScope.launch {

            _uiState.update { currentState ->
                currentState.copy(answers = answer1)
//                (uiState.value.copy(answers = answer1)  )
            }
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
//                _uiState.value.currentQuestionIndex.inc()                 //already done
        viewModelScope.launch {
            _uiState.update {
                it.copy(answers = uiState.value.answers)
            }
        }


        //Submit answer (int)
//                if (answer == event.value) {
//                }
        if (checkIfAllAnswered(uiState.value.questionStateList)) {
            updateQuestion()
        }
    }

    fun onEvent(event: TestEvent, ) {
        val latestState = _uiState.value  //Not needed
        val _currentQuestionIndex = _uiState.value.currentQuestionIndex
        val _listQuestionState =latestState.questionStateList
        val _listAnswerState = latestState.answers
        val lastQuestionState = _listQuestionState[_currentQuestionIndex]

        val endTime =  (System.currentTimeMillis() - start ).toInt()


//        if (latestState != null ) {
//            val question =
//                latestState.questions.first { questionState ->
////                    questionState.questionID == questionId
//                }
//                answer = question.correctAnswer
//            if (answer != null && answer.result is SurveyActionResult.Date) {
//                return answer.result.dateMillis
//            }
//        } else return

        when (event) {

            is TestEvent.Previous -> {


                updateTime(_currentQuestionIndex, endTime) //call update
                _uiState.value.currentQuestionIndex.dec()
//                _uiState.update {
//                    it
//                }
            }

            //TODO If onAppDestroy, should save currentQuestionState.
            is TestEvent.Submit -> {

                updateAnswer(event.value, _currentQuestionIndex, endTime)
//                _uiState.value.currentQuestionIndex.inc()                 //already done
                _uiState.update {
                    it.copy(answers = uiState.value.answers)
                }
                //Submit answer (int)
//                if (answer == event.value) {
//                }
                event.value //
                if (checkIfAllAnswered(uiState.value.questionStateList)) {
                    updateQuestion()
                }
            }

            is TestEvent.Next -> {
                Log.i("TestViewModel ChosAns: ", "answer1:___ " + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString())

                _uiState.value.currentQuestionIndex.inc()
                updateTime(_currentQuestionIndex, endTime)
                Log.i("TestViewModel ChosAns: ", "Next has executed:___ " + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString())
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
        _uiState.update {
            val answerList: List<Answer> = _uiState.value.answers
            answerList[currentQuestIndex].apply {
                timeSpent = timeSpent?.plus(timeOnQuestion)
            }
            it.copy(answers = answerList)
        }
    }

    private fun updateAnswer(answerNbr: Int, currentQuestIndex: Int, timeOnQuestion: Int){
//          TODO Use this when saving and finalizing test.
//        if (answerNbr == _uiState.value.questions[_uiState.value.currentQuestionIndex].correctAnswer){
//        }
        _uiState.value.currentQuestionIndex.inc()
        _uiState.update {
//            _uiState.value.answers[1].chosenAnswer.
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

            val chosenAnsId = _uiState.value.answers[currentQuestIndex].questionId
            val questionState = _uiState.value.questionStateList[currentQuestIndex].copy(
                chosenAnswer = answerNbr
            )
            val question: QuestionState = _uiState.value.questionStateList[currentQuestIndex]
            question.copy(
                chosenAnswer = answerNbr
            )

            it.copy(

                answers = answerList,
                questionStateList = questionList,
            )
        }
    }

    private fun showCurrentQuestion(){
        _uiState.update {
            it.copy()
        }
        val number = _uiState.value.currentQuestionIndex
//        if (number == _uiState.value.questions.)
    }

}

/*
@Composable
private fun SurveyBottomBar(
    questionState: TestState,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        elevation = 7.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (questionState.) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onPreviousPressed
                ) {
                    Text(text = "Previous") //stringResource(id = R.string.previous)
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (questionState.showDone) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onDonePressed,
                    enabled = questionState.enableNext
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            } else {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onNextPressed,
                    enabled = questionState.enableNext
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }
}   */