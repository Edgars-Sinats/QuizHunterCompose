package com.example.quizhuntercompose.feature_pickTest.domain.use_case

data class QuizUseCase (
    val getAllQuestions: GetAllQuestions,
    val getMyQuestions: GetMyQuestions,
    val getXQuestion: GetQuestions,
    val startTest: GetStartTest,
    val updateAnswer: UpdateAnswer,
    val getAllTopics: GetAllTopics
)