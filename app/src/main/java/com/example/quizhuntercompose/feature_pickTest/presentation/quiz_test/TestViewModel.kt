package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.*
import com.example.quizhuntercompose.cor.util.AppConstants.TAG_TEST_VIEW_MODEL
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.TestOptions
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository
import com.example.quizhuntercompose.feature_pickTest.domain.use_case.QuizUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class TestViewModel @Inject constructor(
    private val questionRepository: @JvmSuppressWildcards QuestionRepository,
    private val quizUseCase: @JvmSuppressWildcards QuizUseCase,
    savedStateHandle: @JvmSuppressWildcards SavedStateHandle,
): ViewModel() {
    private lateinit var screenArguments: TestOptions

    private val _uiState =  mutableStateOf( TestState(showPreview = false) )
    val uiState: State<TestState> = _uiState

    private val _currentlySelectAnswer = mutableStateOf<Int?>(null)
    val currentlySelectAnswer: State<Int?> = _currentlySelectAnswer

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var startTimer: Long = 0

    private lateinit var questionStateList: List<QuestionState>
    private lateinit var answerStateList: List<Answer>
    private  var questionCount: Int = 0
    private lateinit var questionIds_: List<Int>
    private var questionNonAns_: Boolean = false
    private var questionWrongAns_: Boolean = false
    private var questionCountForInit: Int = 0
    private var testIdForInit: Int = 0

    init {
        _isLoading.value = true
    }

    fun getTestArguments(arg: String) {
        val moshi: Moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(TestOptions::class.java)!!
        screenArguments = arg.let { jsonAdapter.fromJson(it)!! }
        Log.i( TAG_TEST_VIEW_MODEL, "screenArguments count: ${screenArguments.count}" )

        initializeQuestionsFromDb()
    }

    fun initializeQuestionsFromDb() {
        questionStateList = emptyList()
        answerStateList = emptyList()
        questionCount = screenArguments.count
        questionIds_ = screenArguments.ids
        questionNonAns_ = screenArguments.nonAns
        questionWrongAns_ = screenArguments.wrongAns
//        testIdForInit = screenArguments.testId

        viewModelScope.launch(Dispatchers.IO) {

            val listOfQuestion: List<Question> =
                questionRepository.getMyQuestions(
                    count = questionCount,
                    ids = questionIds_,
                    nonAns = questionNonAns_,
                    wrongAns = questionWrongAns_,
                    testId = screenArguments.testId,
                    time = screenArguments.timeAns)

            listOfQuestion.forEach { question ->

                val newQuestionState = QuestionState(question,
                    chosenAnswer = null,
                    questionStateId = questionCountForInit)
                val newAnswerState = Answer(questionId = questionCountForInit,
                    chosenAnswer = null,
                    timeSpent = null,
                    isFirstQuestion = questionCountForInit == 0,
                    isLastQuestion = questionCountForInit == listOfQuestion.lastIndex)
                answerStateList = answerStateList + newAnswerState
                questionStateList = questionStateList + newQuestionState
                questionCountForInit++
            }

            Log.i(TAG_TEST_VIEW_MODEL, "Loading UI STATE")
            _uiState.value = TestState(
                questionStateList,
                answerStateList,
                answerTime = false,
                showPreview = false,
                currentQuestionIndex = 0,
                wrongAnswerCount = 0,
                correctAnswerCount = 0
            )

            Log.i(TAG_TEST_VIEW_MODEL, "Created _uiState of new question/s")
            _isLoading.value = false
        }
    }

    fun onNewQuestionOpen(){
        startTimer = System.currentTimeMillis()
    }

    //TODO when last question pressed Done, and one of questions is not answered/submitted, it opens question bt with {DONE} instead of {CHOOSE} in Screen
    fun onEvent(event: TestEvent, ) {
        //TODO check start time if answer already has been viewer using Skip. Don`t update time if answer has been answered(like using Previous)
        val currentQuestIndex = _uiState.value.currentQuestionIndex
        val endTime =  (System.currentTimeMillis() - startTimer )
//        println("PRINTING End time: ${endTime}") //plain milliseconds
        println("Current time milliseconds: ${endTime.milliseconds}") //1m 12.341s
//        println("Current time in Whole Sec: ${endTime.milliseconds.inWholeSeconds}") //72
//        println("Current time in Whole Min: ${endTime.milliseconds.inWholeMinutes}") //1
//        println("Current time in Whole Hour: ${endTime.milliseconds.inWholeHours}") //0



        when (event) {

            is TestEvent.ShowDialog -> {
                println("PRINTING ")
                _uiState.value.run { _uiState.value= _uiState.value.copy(showDialog = !_uiState.value.showDialog) }
            }

            is TestEvent.Previous -> {

                if (!_uiState.value.showPreview){
                    updateTime(currentQuestIndex, endTime) //call update
                }
                _uiState.value = _uiState.value.copy(currentQuestionIndex = currentQuestIndex.dec() )
                _currentlySelectAnswer.value = _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer
            }

            //TODO If onAppDestroy, should save currentQuestionState.
            is TestEvent.FinishTestScreen -> {
            }
            is TestEvent.Submit -> {


                if (!_uiState.value.showPreview){
                    updateAnswer(answerNbr = event.value, currentQuestIndex = currentQuestIndex, endTime)
                }

                //Cheking if all quiz questions are submitted
                if ( checkIfAllAnswered(uiState.value.questionStateList) )  {
                    updateQuestion() //update In Database // TODO check how to save answers when app is onDestroy(), at least answers what are answered in the test.
                } else {

                    //Taking next question
                    if (_uiState.value.answers[currentQuestIndex].isLastQuestion) {
                        Log.i("TestViewModel Submit", "all answers should be checked, last answer...")
                        Log.i("TestViewModel Submit", " _uiState.value.answers[currentQuestIndex]... ${_uiState.value.answers[currentQuestIndex]} ")

                        val unansweredQuestId = _uiState.value.questionStateList.first { questionState -> questionState.chosenAnswer == null }.questionStateId
                        _uiState.value = uiState.value.copy(currentQuestionIndex = unansweredQuestId)
                        _currentlySelectAnswer.value = _uiState.value.questionStateList[_uiState.value.currentQuestionIndex].chosenAnswer //TODO check new channges
                        Log.i("TestViewModel Non ans", "Non answered question: $unansweredQuestId" )

                    }
                    else {
                        val nextUnanswered = _uiState.value.questionStateList.first{answer -> answer.chosenAnswer == null }.questionStateId
                        _uiState.value = uiState.value.copy( currentQuestionIndex = nextUnanswered)  //_uiState.value.currentQuestionIndex +1 ) //Show next question!
                        _currentlySelectAnswer.value = _uiState.value.questionStateList[_uiState.value.currentQuestionIndex].chosenAnswer
                        println("PRINTING 2 next question ans: ${uiState.value.answers} \n AND index of question: ${uiState.value.currentQuestionIndex}")
                    }

                }
            }

            //If next is answered, app still take next question, in order to get to unanswered question use middle button, [Done || Find Next]
            is TestEvent.Next -> {

                Log.i("TestViewModel Next: ", "answer1: " + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString())
                if(!_uiState.value.showPreview){
                    updateTime(currentQuestIndex, endTime)
                }

                if (_uiState.value.answers[currentQuestIndex].isLastQuestion) {
                    val unansweredQuestId = _uiState.value.questionStateList.first { questionState -> questionState.chosenAnswer == null }.questionStateId
                    _uiState.value = uiState.value.copy(currentQuestionIndex = unansweredQuestId)
                    _currentlySelectAnswer.value = unansweredQuestId //TODO check new changes

                }else{
                    _uiState.value = _uiState.value.copy( currentQuestionIndex = _uiState.value.currentQuestionIndex.inc() )
                    _currentlySelectAnswer.value = _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer
                }


                Log.i("TestViewModel Next: ", "Next has executed:___\n Chosen (A) answer: " + uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer.toString() + "\n currentQIndex: " + _uiState.value.currentQuestionIndex)
            }

            is TestEvent.AnswerSelected -> {
                println("PRINTING AnswerSelected: ${uiState.value.answers[uiState.value.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.value.currentQuestionIndex}")
                _isLoading.value = true

                _uiState.value.answers[_uiState.value.currentQuestionIndex].chosenAnswer = event.value
                _currentlySelectAnswer.value = event.value

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
        var _correctAnswer = 0
        var _questionStatePosition = 0

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
                        lastAnswerTime = lastAnsTime.toInt(),
                        correctAnswers =
                        if (_currentQuestionState.chosenAnswer == _currentQuestion.correctAnswer-1) {
                            it.question.correctAnswers+1
                        } else {
                            it.question.correctAnswers
                        },

                        nonAnswers =
                        if (lastAnsTime == (1).toLong() && (_currentQuestionState.chosenAnswer != _currentQuestion.correctAnswer-1)) {
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
                        averageAnswerTime = (( _totalAnsTime + lastAnsTime ) / ( answerTimes+1 )).toInt(),
                        /* TODO I might want to change last answer time value more valuable once more questions has been answered.
                         So the average ans time change with bigger impact when multiple answers already answered.
                         For example when will choose in testChoose screen, based on answer time.,
                         if some question has been answered multiple times with long ans time, and last time it was answered quickly,
                         it show person know the answer finally well, and should be skipped or ranked lower for choosing question.
                        */
                    )//_uiState
                )//updateQuestion
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            }
            _questionStatePosition += 1
        }
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
     * @param QuestionState.chosenAnswer has null value - one question has not been answered
     * Return @param true if all questions has been answered, false otherwise.
     *
     */

    private fun checkIfAllAnswered(questionList: List<QuestionState>) : Boolean{
        //if NotNull(QuestSFound) -> no ans(for one).       // null -> non answered
        return questionList.find {
                questionState ->
                    questionState.chosenAnswer == null      //Some unanswered
        } == null // All answered &&
//                   questionState.questionStateId != lastQuestionId
    }


    private fun updateTime(currentQuestIndex: Int,timeOnQuestion: Long){
        val answerList: List<Answer> = _uiState.value.answers
        answerList[currentQuestIndex].apply {
            timeSpent = timeSpent?.plus(timeOnQuestion)
        }
        _uiState.value = _uiState.value.copy(answers = answerList)
    }

    private fun updateAnswer(answerNbr: Int, currentQuestIndex: Int, timeOnQuestion: Long){

        val answerList: List<Answer> = _uiState.value.answers
        val questionList: List<QuestionState> = _uiState.value.questionStateList

        answerList.elementAt(currentQuestIndex).apply {
            chosenAnswer = answerNbr
            timeSpent = timeSpent?.plus(timeOnQuestion) ?: timeOnQuestion
        }

        questionList.elementAt(currentQuestIndex).apply {
            chosenAnswer = answerNbr
        }

        _uiState.value = _uiState.value.copy(
            answers = answerList,
            questionStateList = questionList,
        )
    }


}


