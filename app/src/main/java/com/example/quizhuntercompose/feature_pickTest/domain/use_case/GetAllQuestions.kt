package com.example.quizhuntercompose.feature_pickTest.domain.use_case

import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository

//TODO Inject constructor?
class GetAllQuestions(
    private val repository: QuestionRepository
) {
    operator fun invoke(testId: Int): List<Question?> {
        return repository.getAllQuestions(testId)
    }
}