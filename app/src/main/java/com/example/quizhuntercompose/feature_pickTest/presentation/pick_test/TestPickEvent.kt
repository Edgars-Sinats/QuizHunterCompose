package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test

sealed class TestPickEvent {
    data class StartQuiz(val value: String): TestPickEvent() //ChosenOption? Need new class or put existing => QuizUse Case
    data class ChooseCount(val value: Int) : TestPickEvent()

    object OpenOptions: TestPickEvent()
    object UploadTestQuestions: TestPickEvent()
    object DownloadTestQuestions: TestPickEvent()
    data class PickUnanswered(val value: Boolean) : TestPickEvent()
    data class PickWrongAnswered(val value: Boolean) : TestPickEvent()
    data class PickTime(val value: Boolean) : TestPickEvent()
    data class CheckTopics(val topic: Int) : TestPickEvent()
    data class CheckAllTopics(val value: Boolean) : TestPickEvent()
    data class CheckTopicQuestionCount(val topic: Int): TestPickEvent()
    data class CheckTopicsQuestionsCount(val topic: List<Int>): TestPickEvent()
//    getQuestionCountFrom

}

