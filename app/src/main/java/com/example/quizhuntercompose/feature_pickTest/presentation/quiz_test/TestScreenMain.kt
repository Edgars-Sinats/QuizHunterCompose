package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test

import android.content.res.Configuration
import android.provider.MediaStore.Video
import android.util.Log
import android.widget.MediaController
import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.quizhuntercompose.R
import com.example.quizhuntercompose.components.ProgressBar
import com.example.quizhuntercompose.cor.util.AppConstants.TAG_TEST_SCREEN
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.util.supportWideScreen
import com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test.components.VideoPlayerScreen
import com.example.quizhuntercompose.ui.theme.QuizHunterComposeTheme
import com.example.quizhuntercompose.ui.theme.slightlyDeemphasizedAlpha

private const val CONTENT_ANIMATION_DURATION = 700

@Composable
//@ExperimentalComposeUiApi
@ExperimentalAnimationApi
fun TestScreenMain(
    uiState: TestState,
    onSelectAnswer: (TestEvent) -> Unit,
    onNextPressed: (TestEvent) -> Unit,
    onDonePressed: (TestEvent) -> Unit,
    onPreviousPressed: (TestEvent) -> Unit,
    currentlySelectedAnswer: State<Int?>,
    navigateToFinish: () -> Unit,
//        testViewModel: TestViewModel,
//        onBackPressedCallback: OnBackPressedCallback

) {
    Surface(modifier = Modifier.supportWideScreen()) {

        Scaffold(

            topBar = {}, //TODO
            content = { padding ->
                AnimatedContent(
                    targetState = remember { mutableStateOf(uiState.currentQuestionIndex) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    transitionSpec = {
                        val animationSpec: TweenSpec<IntOffset> = tween(CONTENT_ANIMATION_DURATION)

                        if (targetState.value > initialState.value ) {
                            slideIntoContainer(
                                animationSpec = animationSpec,
                                towards = AnimatedContentTransitionScope.SlideDirection.Left
                            ) togetherWith
                                    fadeOut ( animationSpec = tween(600) )
                        } else if (targetState.value == initialState.value) {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Up, animationSpec
                            ) togetherWith fadeOut(animationSpec = tween(600))
                        } else {
                            fadeIn(
                                animationSpec = tween(1200)
                            ) togetherWith slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = animationSpec
                            )
                        }.apply {
                            targetContentZIndex = targetState.value.toFloat()
                        }
                    }, label = ""
                )
                { animationTargetState ->      //targetState ->
                    if (animationTargetState.value == 0) {
                        println("PRINTING ANSWER: ${uiState.answers[uiState.currentQuestionIndex].chosenAnswer}")
                    }
                    QuestionScreen( //Question (+ answers)
//                            selectedAnswer = selectedAnswer1,
                        answer = uiState.answers[uiState.currentQuestionIndex], // TODO create unit test // Answer list declared in TestState itself already.
                        onAnswer = {it->
                            println("PRINTING AnswerSelected_screen1: ${uiState.answers[uiState.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.currentQuestionIndex}")
                            onSelectAnswer(TestEvent.AnswerSelected(it))
//                                testViewModel.onEvent(TestEvent.AnswerSelected(it))
                            println("PRINTING AnswerSelected_screen2: ${uiState.answers[uiState.currentQuestionIndex].chosenAnswer} \n AND index of question: ${uiState.currentQuestionIndex}")

                        },
                        question = uiState.questionStateList[uiState.currentQuestionIndex].question,
                        chosenAnswerState = uiState.questionStateList[uiState.currentQuestionIndex].chosenAnswer != null,
                        selectedAnswer = currentlySelectedAnswer,
                        showPreview = uiState.showPreview
                    )
                }//TargetState
            },
            bottomBar = {
                AnimatedContent(targetState = uiState, label = "") {
                        targetState1->
                    if (targetState1.showPreview){
                        //todo
                    }
                    SurveyBottomBar(
                        selectedAnswer = currentlySelectedAnswer,
                        answerState = uiState.answers[uiState.currentQuestionIndex],
                        onPreviousPressed = {
                            onPreviousPressed(TestEvent.Previous)
                        },
                        onNextPressed = {
                            onNextPressed(TestEvent.Next)
                        }, //SKIP
                        onDonePressed = { //Submit\Finish
                            if (uiState.showPreview){
                                navigateToFinish.invoke()
                            }else{
                                onDonePressed(TestEvent.Submit(uiState.answers[uiState.currentQuestionIndex].chosenAnswer!!))
                            }
                        },
                        showPreview = uiState.showPreview
                    )
                }
            } //BottomBar
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TestScreen(
    testViewModel: TestViewModel = hiltViewModel(),
    navigateToFinish: () -> Unit,
    startingQuestionArg: String,
){
    LaunchedEffect(Unit) {
        Log.i(TAG_TEST_SCREEN, "getTestArguments in progress...")
        testViewModel.getTestArguments(startingQuestionArg)
    }
    val uiState = testViewModel.uiState.value // Did not work with .collectAsStateWithLifecycle() or //better then just .collectAsState()  https://medium.com/androiddevelopers/consuming-flows-safely-in-jetpack-compose-cde014d0d5a3
    val isLoading by testViewModel.isLoading.collectAsState()

    if (uiState.showDialog){
        QuizHunterComposeTheme {
            PopUpDialog(
                onClosePreviewScreen = navigateToFinish,
                onDismiss =  testViewModel::onEvent,
                dialogState = uiState.showDialog,
                correctCount = uiState.correctAnswerCount,
                wrongCount = uiState.wrongAnswerCount
            )
        }
    }

    if (!isLoading ) {
        TestScreenMain(
            uiState = testViewModel.uiState.value,
            onSelectAnswer = testViewModel::onEvent,
            onNextPressed = testViewModel::onEvent,
            onPreviousPressed = testViewModel::onEvent,
            onDonePressed = testViewModel::onEvent,
            currentlySelectedAnswer = testViewModel.currentlySelectAnswer,
            navigateToFinish = navigateToFinish
        )
        if (!uiState.showDialog){
            testViewModel.onNewQuestionOpen()
        }

    } else {
        ProgressBar()
    }
}

@Composable
private fun SurveyBottomBar(
    answerState: Answer,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit,
    selectedAnswer: State<Int?>,
    showPreview: Boolean,
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
                    Text(
                        text = stringResource(id = R.string.previous),
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            // Chose/Done
            if (showPreview) {
                Button (
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onDonePressed,
                ) {
                    Text(text = stringResource(id = R.string.close_preview))
                }
                Spacer(modifier = Modifier.width(16.dp))
            } else if ( answerState.chosenAnswer == null && selectedAnswer.value == null ) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = { /* Disabled */ },
                ) {
                    Text(text = stringResource(id = R.string.choose))
                }
                Spacer(modifier = Modifier.width(16.dp))
            } else{
                Log.i(TAG_TEST_SCREEN, "ChosenAnswer is NOT NULL ! " +
                        "\n Answer State questionId: " + answerState.questionId.toString() +
                        "\n Answer state chosen answer: " + answerState.chosenAnswer )

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
                        Log.i(TAG_TEST_SCREEN, "answerState.isLastQuestion: " +
                                answerState.isLastQuestion )
                    },
                ) {
                    if (answerState.chosenAnswer != null || selectedAnswer.value != null ) {
                        Text(text = stringResource(R.string.non)) //TODO
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
                ) {
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
    showPreview: Boolean,
){
    Column(modifier = Modifier.padding (all = 8.dp) ) {
        Spacer(modifier = Modifier.height(32.dp))
        Log.i(TAG_TEST_SCREEN, "Question img: ${question.imageUrl}")

        if (question.imageUrl != null && question.imageUrl != ""){
            if (question.imageUrl.endsWith(".mp4") ){
                Box(modifier = Modifier.height(120.dp)) {
                    VideoPlayerScreen(uriLink = question.imageUrl)
                }
            }

            AsyncImage(
                model = question.imageUrl,
                contentDescription = "question image",
                placeholder = painterResource(id = R.drawable.ic_outline_list_24),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(4.dp),
            )

        }
        QuestionTitle(question.question
//                + " "
//                + "\n\n And last answer.ChosenAnswer: " + answer.chosenAnswer
//                + "\n SelectedAnswer: " + selectedAnswer.value
//                + "\n QuestionId: " + answer.questionId
//                + "\n ShowPreview: " + showPreview
//                + "\n Correct answer: " + (question.correctAnswer-1)
        )
        Spacer(modifier = Modifier.height(24.dp))

        QuestionAnswers(
            question = question,
            answer = answer,
            onAnswerSelected = { answer -> onAnswer(answer) }, //TODO check //Unit test. Should i put on onsAnswer on update it
//            chosenAnswerState = chosenAnswerState,
            modifier = Modifier.fillMaxWidth(),
            showPreview = showPreview
        )
    }
}

/**
 *  Display a question title.
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
                .padding(vertical = 24.dp, horizontal = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize =  16.sp
        )
    }
}

/**
 *  Display an answer list.
 *
 *  @param answer (state) is answer currently visible.
 *  @param onAnswerSelected (event) change selected topic. //If topic is alrady inState
// *  @param chosenAnswerState (state) Question state - submitted answer. -> For Submit button
 */
@Composable
private fun QuestionAnswers(
    question: Question,
    answer: Answer,
    onAnswerSelected: (Int) -> Unit,

    modifier: Modifier = Modifier,
    showPreview: Boolean,
) {

    val allAnswers = listOf(question.answer1, question.answer2, question.answer3, question.answer4 )
    val radioOptionsCheckList:MutableList<String> = mutableListOf()

    for (element in allAnswers) {
        if (element != null && element != "") {
            radioOptionsCheckList.add(element)
        }
    }
    val (selectedOption, onOptionSelected) = remember(answer) { mutableStateOf(answer.chosenAnswer) }

    val radioOptions:List<String> = radioOptionsCheckList


    LazyColumn(modifier = modifier) {

        item {  radioOptions.forEachIndexed { index, text ->
            val onClickHandle = {
                onOptionSelected(index)
                radioOptions[index].let { onAnswerSelected(index) }
                Unit
            }
            val optionSelected = index == selectedOption
            val answerBorderColor: Color
            val answerBackgroundColor: Color

            if(showPreview) {

                answerBorderColor =
                if (optionSelected && answer.chosenAnswer == question.correctAnswer-1) {
                    MaterialTheme.colors.secondaryVariant.copy(alpha = 1f) //Green
                } else if (optionSelected){
                    MaterialTheme.colors.error.copy(alpha = 0.5f) //
                }
                else {
                    MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                }


                answerBackgroundColor =
                //Selected correct answer
                if (optionSelected && answer.chosenAnswer == question.correctAnswer-1) {
                    Color.Green.copy(alpha = 0.65f)
                }
                //Wrong
                else if ( optionSelected) {
                    MaterialTheme.colors.error.copy(alpha = 0.12f)//Red
                }
                //Show correct answer
                else if (index == question.correctAnswer-1 ){
                    Color.Green.copy(alpha = 0.6F) //MaterialTheme.colors.secondaryVariant.copy(alpha = .5f) //Green
                }
                //Regular background
                else if (index != question.correctAnswer-1) {
                    MaterialTheme.colors.background
                }
                //Should not be visible
                else {
                    MaterialTheme.colors.onError.copy(alpha = 0.1f)
                }

            }
            else {
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
                    if (optionSelected) {
                        2.dp
                    } else {
                        1.dp
                    },
                    color = answerBorderColor
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { !showPreview }
                        .selectable(
                            selected = optionSelected,
                            onClick = onClickHandle,
                            enabled = !showPreview
                        )
                        .background(answerBackgroundColor)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = text,
                    )
                    RadioButton(
                        selected = optionSelected,
                        onClick = if(!showPreview){
                            onClickHandle
                        } else {
                            {/* Disabled - show preview only */}
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
        } //End radOptKeys
        }//End item [Lazy]
    }
}

@Composable
fun PopUpDialog(
    onClosePreviewScreen: () -> Unit,
    onDismiss: (TestEvent) -> Unit,
    dialogState: Boolean,
    onDialogStateChange: ((Boolean) -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null,
    correctCount: Int,
    wrongCount: Int,
){
    val dialogShape = RoundedCornerShape(16.dp)
    val dialogImageBitmap:Unit



    if (dialogState){

        AlertDialog(
            onDismissRequest = {
                //TODO close all test. Open choose test. (Option of Show previous test => Don`t close last testViewModel, clean it up on open?) ?
                onDialogStateChange?.invoke(false)
                onDismissRequest?.invoke()
            },
            buttons = {
                      Row(
                          modifier = Modifier.fillMaxWidth(),

                        verticalAlignment =  Alignment.CenterVertically) {

                          TextButton(onClick = {onClosePreviewScreen.invoke() } ) {
                              Text(text = stringResource( id = R.string.dismiss ))
//                              onDialogStateChange?.invoke(false)
//                              onDismissRequest?.invoke()
//                              onDismiss(TestEvent.ShowDialog)
                          }
                          Spacer(Modifier.weight(0.2f))

                          TextButton(
                              onClick = { onDismiss(TestEvent.ShowDialog) } ) {
                              Text(text = stringResource(id = R.string.review_answers))
                          }

                      }
            },
            title = {
                Column(Modifier
                    .padding(16.dp, top = 64.dp)
                    .fillMaxWidth(), Arrangement.SpaceBetween, horizontalAlignment = CenterHorizontally) {
                    val titleText:String

                    if ( wrongCount == 0 && correctCount > 0 ){
                        titleText= stringResource( id = R.string.excellent )
                        Image(

                            painter = painterResource(id = R.drawable.ic_baseline_sentiment_very_satisfied_24),
                            contentDescription = stringResource( id = R.string.ic_baseline_sentiment_very_satisfied_24 )
                        )
                    }
                    else if ( correctCount > wrongCount ){
                        titleText= stringResource( id = R.string.good )
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_sentiment_satisfied_alt_24),
                            contentDescription = stringResource( id = R.string.ic_baseline_sentiment_satisfied_alt_24 )
                        )
                    }
                    else if ( wrongCount == 0 && correctCount == 0 ) {
                        titleText= stringResource( id = R.string.something_went_wrong )
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_error_24),
                            contentDescription = stringResource( id = R.string.ic_baseline_error_24 )
                        )
                    }
                    else {
                        titleText= stringResource( id = R.string.not_met_expectation )
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_sentiment_dissatisfied_24),
                            contentDescription = stringResource( id = R.string.ic_baseline_sentiment_dissatisfied )
                        )
                    }

                    Text(text = titleText)
                }
            },

            text = {
                Text( text =
                        stringResource(id = R.string.you_have_answered)
                                + " " + correctCount + " " +
                        stringResource(id = R.string.questions_correctly_of_total)
                                + (wrongCount + correctCount) + "." )
                   },
            shape = dialogShape,

        )

    } //dialogState

}

val question1 = Question(
    questionID= 1,
    testID = 1,
    question= "Lets try out in view how much we can expend this  question Question. Lets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question QuestionLets try out in view how much we can expend this  question Question",
    answer1= "Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1. Question ans nbr. 1.Question ans nbr. 1.",
    answer2= "2. Weary long text with no hidden meaning but only for test purposes. ",
    answer3 = "3",
    answer4 = "Small next preview",
    correctAnswer = 2,
    topic = 1,
    explanation = null,
    correctAnswers = 1, //make sure in initalizer it is = 0
    wrongAnswers = 1,
    nonAnswers = 0,
    averageAnswerTime = 21,
    lastAnswerTime = 2,
    imageUrl = ""
)
val selectedAnsPreview: Int = 2

val answer1 = Answer(questionId = 2, chosenAnswer = selectedAnsPreview, timeSpent = 201, isFirstQuestion = false, isLastQuestion = true)
var selectedAnswerPreview: State<Int?>  = mutableStateOf(2)

@Preview
@Preview("DarkScreen Question Preview" , uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QuestionScreenPreview(){
    QuizHunterComposeTheme() {
        QuestionScreen(
            answer = answer1,
            onAnswer = {},
            question = question1,
            chosenAnswerState = true,
            selectedAnswer = selectedAnswerPreview,
            showPreview = true
        )
    }

}

@Preview
@Preview("DarkScreen AnswerPrev" , uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QuestionAnswersPreview(){

    QuizHunterComposeTheme {
        QuestionAnswers(
            question = question1,
            answer = answer1,
            onAnswerSelected = {answer1.chosenAnswer = it},
//            chosenAnswerState = true,
            showPreview = true
        )
    }
}

@Preview("LightScreen")
@Preview("DarkScreen" , uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun popUpDialogPreview(){

    QuizHunterComposeTheme{
        PopUpDialog(
            onClosePreviewScreen = {},
            onDismiss = {},
            dialogState = true,
            correctCount = 3,
            wrongCount = 5
        )
    }
}

@Preview
@Composable
fun SurveyBottomBarPreview(){
    val answerNbr: State<Int?>  = remember { mutableStateOf<Int?>(2) }
    SurveyBottomBar(
        onDonePressed = {},
        onPreviousPressed = {},
        onNextPressed = {},
        selectedAnswer = answerNbr,
        answerState = Answer(questionId = 2, chosenAnswer = 2, timeSpent = 201, isFirstQuestion = false, isLastQuestion = true),
        showPreview = false
    )
}
