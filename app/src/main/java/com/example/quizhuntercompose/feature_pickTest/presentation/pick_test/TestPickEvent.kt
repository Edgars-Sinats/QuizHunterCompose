package com.example.quizhuntercompose.feature_pickTest.presentation.pick_test

sealed class TestPickEvent {
    object StartQuiz: TestPickEvent()
    data class ChooseCount(val value: Int) : TestPickEvent()

    object OpenOptions: TestPickEvent()
    data class PickUnanswered(val value: Boolean) : TestPickEvent()
    data class PickWrongAnswered(val value: Boolean) : TestPickEvent()
    data class PickTime(val value: Boolean) : TestPickEvent()
    data class ChooseTopics(val topics: List<Int>) : TestPickEvent()

}

