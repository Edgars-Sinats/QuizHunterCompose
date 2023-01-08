package com.example.quizhuntercompose.feature_pickTest.presentation.quiz_test

sealed class TestEvent {
    object Previous: TestEvent()
    data class Submit(val value: Int): TestEvent()
    object Next: TestEvent()
    data class AnswerSelected(val value: Int): TestEvent()
    object ShowResults: TestEvent()
}
