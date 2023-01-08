package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.util.supportWideScreen
import com.example.quizhuntercompose.ui.theme.slightlyDeemphasizedAlpha

private const val CONTENT_ANIMATION_DURATION = 500

@Composable
fun TestRoute(
    testViewModel: TestViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navigateToFinish: (String) -> Unit
){

    val selectedAnswer1 by testViewModel.currentlySelectAnswer
    val uiState = testViewModel.uiState.value // Did not work with .collectAsStateWithLifecycle() or //better then just .collectAsState()  https://medium.com/androiddevelopers/consuming-flows-safely-in-jetpack-compose-cde014d0d5a3
//    val uiState = uiState1.value
    val isLoading by testViewModel.isLoading.collectAsState()

    if (!isLoading ) {
//        Log.i("TestScreen_:", "isLoading DONE, \n uiState (questionStateID: " + uiState.questionStateList[uiState.currentQuestionIndex].questionStateId.toString()
//         + " \n uiState - questionStateList (chosen answer): " + uiState.questionStateList[uiState.currentQuestionIndex].chosenAnswer.toString()
//        + " \n uiState - answer -(chosenAnswer):     " + uiState.answers[uiState.currentQuestionIndex].chosenAnswer.toString()
//        )
            TestScreen(
//        selectedAnswer1 = selectedAnswer1,
        uiState = testViewModel.uiState.value,
        onSelectAnswer = testViewModel::onEvent,
        onNextPressed = testViewModel::onEvent,
        onPreviousPressed = testViewModel::onEvent,
        onDonePressed = testViewModel::onEvent,
        testViewModel = testViewModel
        )

    } else {
        //TODO create and implement loading circle
        Text(
            text = ("Is loading is true"),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TestScreen(
//        selectedAnswer1: Int?,
        uiState: TestState,
        onSelectAnswer: (TestEvent) -> Unit,
        onNextPressed: (TestEvent) -> Unit,
        onDonePressed: (TestEvent) -> Unit,
        onPreviousPressed: (TestEvent) -> Unit,
        testViewModel: TestViewModel,

    ) {
        Surface(modifier = Modifier.supportWideScreen()) {
            Scaffold(

                topBar = {}, //TODO
                content = { //InnerPadding ->
                    AnimatedContent(
                        targetState = uiState,
                        transitionSpec = {
                            val animationSpec: TweenSpec<IntOffset> = tween(CONTENT_ANIMATION_DURATION)

                            val direction =
                                if (targetState.questionStateList[uiState.currentQuestionIndex].questionStateId > initialState.questionStateList[uiState.currentQuestionIndex].questionStateId) {
                                    // Going forwards in the survey: Set the initial offset to start
                                    // at the size of the content so it slides in from right to left, and
                                    // slides out from the left of the screen to -fullWidth
                                    AnimatedContentScope.SlideDirection.Left
                                } else {
                                    // Going back to the previous question in the set, we do the same
                                    // transition as above, but with different offsets - the inverse of
                                    // above, negative fullWidth to enter, and fullWidth to exit.
                                    AnimatedContentScope.SlideDirection.Right
                                }
                            slideIntoContainer(
                                towards = direction,
                                animationSpec = animationSpec
                            ) with
                                    slideOutHorizontally(
                                        animationSpec = tween(CONTENT_ANIMATION_DURATION),
                                        targetOffsetX = { fullWidth ->  -fullWidth }
                                    )

                        }

                    ) {//targetState ->
                        QuestionScreen( //Question (+ answers)
//                            selectedAnswer = selectedAnswer1,
                            answer = uiState.answers[uiState.currentQuestionIndex], // TODO create unit test // Answer list declared in TestState itself already.
                            onAnswer = {
                                println("PRINTING AnswerSelected_screen1: ${uiState.answers[uiState.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.currentQuestionIndex}")
                                onSelectAnswer(TestEvent.AnswerSelected(it))
//                                testViewModel.onEvent(TestEvent.AnswerSelected(it))
                                println("PRINTING AnswerSelected_screen2: ${uiState.answers[uiState.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.currentQuestionIndex}")

                            },
                            question = uiState.questionStateList[uiState.currentQuestionIndex].question,
                            chosenAnswerState = uiState.questionStateList[uiState.currentQuestionIndex].chosenAnswer != null,
                            selectedAnswer = testViewModel.currentlySelectAnswer,
                            showPreview = uiState.showPreview

//                            //Bottom bar
//                            selectedAnswer = testViewModel.currentlySelectAnswer,
//                            answerState = uiState.answers[uiState.currentQuestionIndex],
//                            onPreviousPressed = {
//                                onPreviousPressed(TestEvent.Previous)
//                                println("PRINTING AnswerSelected: ${uiState.answers[uiState.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.currentQuestionIndex}")
////                                println("PRINTING_TargetState!!!: ${uiState.answers[uiState.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.currentQuestionIndex}")
//
//                            }, // testViewModel.onEvent(TestEvent.Previous) }, //Where I need to change
//
//                            onNextPressed = {
//                                onNextPressed(TestEvent.Next)
////                                testViewModel.onEvent(TestEvent.Next)
//                            }, //SKIP
//
//                            onDonePressed = { //Submit
//                                onDonePressed(TestEvent.Submit(uiState.answers[uiState.currentQuestionIndex].chosenAnswer!!))
//                                uiState.questionStateList[uiState.currentQuestionIndex].chosenAnswer = uiState.answers[uiState.currentQuestionIndex].chosenAnswer //TODO what is that, do I need it before viewModel, or inside viewModel?
//                            }
                        )
                    }//TargetState
                },
                bottomBar = {
                    AnimatedContent(targetState = uiState) {

                            targetState1->
                        SurveyBottomBar(
                            selectedAnswer = testViewModel.currentlySelectAnswer,

                            answerState = uiState.answers[uiState.currentQuestionIndex],
                            onPreviousPressed = {
                                onPreviousPressed(TestEvent.Previous)
                                println("PRINTING AnswerSelected: ${uiState.answers[uiState.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.currentQuestionIndex}")
                                println("PRINTING_TargetState!!!: ${targetState1.answers[uiState.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.currentQuestionIndex}")

                            }, // testViewModel.onEvent(TestEvent.Previous) }, //Where I need to change

                            onNextPressed = {
                                onNextPressed(TestEvent.Next)
//                                testViewModel.onEvent(TestEvent.Next)
                            }, //SKIP

                            onDonePressed = { //Submit
                                onDonePressed(TestEvent.Submit(uiState.answers[uiState.currentQuestionIndex].chosenAnswer!!))
                                uiState.questionStateList[uiState.currentQuestionIndex].chosenAnswer = uiState.answers[uiState.currentQuestionIndex].chosenAnswer //TODO what is that, do I need it before viewModel, or inside viewModel?
                            }
                        )
                    }

                } //BottomBar
            )
        }
}

@Composable
private fun SurveyBottomBar(
//    questionState: QuestionState,
    answerState: Answer,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit,
    selectedAnswer: State<Int?>
    ) {

//Bottom option navigation
    Surface(
        modifier = Modifier.fillMaxWidth(),
        elevation = 7.dp,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {

            //Previous
            if (answerState.isFirstQuestion) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = {/* TODO Not supposed to work. All good. Need design change? */ }
                ) {
                    Text(text = stringResource(id = R.string.previous))
                }
                Spacer(modifier = Modifier.width(16.dp))
            } else {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onPreviousPressed
                ) {
                    Text(text = stringResource(id = R.string.previous),
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            // Chose/Done
            if ( answerState.chosenAnswer == null && selectedAnswer.value == null ) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = { /* Disabled, -> TODO prompt? choose an answer */
                        Log.i("TestScreen", "ChosenAnswer is null, choose an answer. Answer State questionId: "
                                + answerState.questionId.toString() + "\n Answer state chosen answer:9 " + answerState.chosenAnswer )},
                ) {
                    Text(text = stringResource(id = R.string.choose))
//                    if (questionState.isLastQuestionInTest) {
//                    } else {
//                        Text(text = stringResource(id = R.string.done))
//                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
            } else {
                Log.i("TestScreen", "ChosenAnswer is NOT NULL !!!!!  " +
                        "Answer State questionId: " + answerState.questionId.toString() +
                        "\n Answer state chosen answer:9 " + answerState.chosenAnswer )

                Button(
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp),
                    onClick = onDonePressed,
//                    enabled = questionState.enableNext
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

            //Skip
            if ( answerState.isLastQuestion ) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = { /* TODO Not supposed to work. All good. Need design change? */
                        Log.i("TestScreen", "answerState.isLastQuestion: " +
                                answerState.isLastQuestion )
                    },
//                    enabled = questionState.enableNext
                ) {
                    if (answerState.chosenAnswer != null || selectedAnswer.value != null ) {
                        Text(text = " Non ")
                    } else {
                        Text(text = stringResource(id = R.string.done))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
            } else {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onNextPressed,
//                        {
//                            Log.i("TestScreen", "answerState.isLastQuestion: " +
//                                    answerState.isLastQuestion )
//                            onNextPressed
//                                  }, //TODO Or to jump to unanswered. /ViewModel - if(checkIfAllAnswered(uiState.value.questionStateList))
//                    enabled = true //When last question., button disabled
//                        enabled = true
                ) {
//                        Text(text = "SKIP")
                    if (answerState.chosenAnswer != null) {
                        Text(text = stringResource(id = R.string.next))
                    } else {
                        Text(text = stringResource(id = R.string.skip))
                    }
                }
            }
        }
    } //Surface
}

/**
 * Display screen with title and answers.
 *
 * @param question (state - in-mutable) Question object
 * @param answer (state)
 * @param onAnswer (event) -> request state change in answer and composable(inside func) color/font change.
 * @param chosenAnswerState (state) - is true if answer has been selected in QuestionState. =>
 *                          Show green or red based on this on preview. Disable submit button.
 */
@Composable
fun QuestionScreen(
    answer: Answer,
    onAnswer: (Int) -> Unit,
    question: Question,
    chosenAnswerState: Boolean,
    selectedAnswer: State<Int?>,
    showPreview: Boolean
//    answerState: Answer,
//    onPreviousPressed: () -> Unit,
//    onNextPressed: () -> Unit,
//    onDonePressed: () -> Unit

){
    Column() {
        // QuestionList
        LazyColumn(){
            item {
                Spacer(modifier = Modifier.height(32.dp))
                QuestionTitle(question.question + " "
                        + "\n\n And last answer.ChosenAnswer: " + answer.chosenAnswer
                        + "\n SelectedAnswer: " + selectedAnswer.value
                        + "\n QuestionId: " + answer.questionId
                        + "\n ShowPreview: " + showPreview
                        + "\n Correct answer: " + (question.correctAnswer-1)

                )
                Spacer(modifier = Modifier.height(24.dp))

                //TODO add preview for answer , when browsing, show correct[GREEN] answer and wrong[RED].
                //TODO make this scrollable (does lazy list already implement this?)
                QuestionAnswers(
                    question = question,
                    answer = answer,
                    onAnswerSelected = { answer -> onAnswer(answer) }, //TODO check //Unit test. Should i put on onsAnswer on update it
                    chosenAnswerState = chosenAnswerState,
                    modifier = Modifier.fillParentMaxWidth(),
                    showPreview = showPreview
                )
            }
        }

    }


}

/**
 * Display a question title.
 */
@Composable
private fun QuestionTitle( title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            text = (title),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
    }
}

/**
 *  Display an answer list.
 *
 *  @param answer (state) is answer currently visible.
 *  @param onAnswerSelected (event) change selected topic. //If topic is alrady inState
 *  @param chosenAnswerState (state) Question state - submitted answer. -> For Submit button
 */
@Composable
private fun QuestionAnswers(
    question: Question,
    answer: Answer,
    onAnswerSelected: (Int) -> Unit,
    chosenAnswerState: Boolean, //TODO if true buttons disabled. // Create extra attribute showPreview() and show red/green answers.
    modifier: Modifier = Modifier,
    showPreview: Boolean
) {

    val allAnswers = listOf(question.answer1, question.answer2, question.answer3, question.answer4 )
    val radioOptionsCheckList:MutableList<String> = mutableListOf()
    for (element in allAnswers) {
        if (element != null && element != "") {
//            Log.i("TestScreen RadioButt", "Rad button: " + element.toString() )
            radioOptionsCheckList.add(element)
        }
    }
    val (selectedOption, onOptionSelected) = remember(answer) { mutableStateOf(answer.chosenAnswer) }

    val radioOptions:List<String> = radioOptionsCheckList



    Column(modifier = modifier) {

        //TODO implement them. //Can delete-  val radOptKeys =
        val radOptKeys = radioOptions.forEachIndexed { index, text -> //TODO use this to color the UI composable
            val onClickHandle = {
                onOptionSelected(index)
                radioOptions[index].let { onAnswerSelected(index) }
                Unit
            }
            val optionSelected = index == selectedOption
            val answerBorderColor: Color
            val answerBackgroundColor: Color
            if(showPreview) {
                answerBorderColor = if (optionSelected && answer.chosenAnswer == question.correctAnswer-1) {
                    MaterialTheme.colors.onPrimary.copy(alpha = 0.5f) //Green
                } else if (optionSelected){
                    MaterialTheme.colors.error.copy(alpha = 0.5f) //
                }
                else {
                    MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                }

                answerBackgroundColor = if (optionSelected && answer.chosenAnswer == question.correctAnswer-1) {
                    MaterialTheme.colors.onPrimary.copy(alpha = 0.12f) //Green

                } else if( optionSelected) {
                    MaterialTheme.colors.error.copy(alpha = 0.12f)
                }
                //Show correct answer
                else if (answer.chosenAnswer != question.correctAnswer ){
                    MaterialTheme.colors.onPrimary.copy(alpha = 0.12f) //Green
                }
                else {
                    MaterialTheme.colors.background
                }


            }else {
                answerBorderColor = if (optionSelected) {
                    MaterialTheme.colors.primary.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                }

                answerBackgroundColor = if (optionSelected) {
                    MaterialTheme.colors.primary.copy(alpha = 0.12f)
                } else {
                    MaterialTheme.colors.background
                }
            }


            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = optionSelected,
                            onClick = onClickHandle
                        )
                        .background(answerBackgroundColor)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = text,
//                        style = MaterialTheme.typography.subtitle2,
//                        color = MaterialTheme.colors.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 24.dp, horizontal = 16.dp)
                    )
                    RadioButton(
                        selected = optionSelected,
                        onClick = onClickHandle,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun TestScreenPreview(){
////    TestScreen(
////        uiState = TestState(),
////
////    )
//
//}

@Preview
@Composable
fun SurveyBottomBarPreview(){
    val answernbr: State<Int?>  = remember { mutableStateOf<Int?>(2) }
    SurveyBottomBar(
//        questionState = QuestionState(
//            question = question1,
//            chosenAnswer = null,
//            questionStateId = 2
//        ),
        onDonePressed = {},
        onPreviousPressed = {},
        onNextPressed = {},
        selectedAnswer = answernbr,
        answerState = Answer(questionId = 2, chosenAnswer = 2, timeSpent = 201, isFirstQuestion = false, isLastQuestion = true)
    )
}
