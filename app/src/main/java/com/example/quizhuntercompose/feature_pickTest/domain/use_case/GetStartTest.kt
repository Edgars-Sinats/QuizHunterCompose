package com.example.quizhuntercompose.feature_pickTest.domain.use_case

import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository

class GetStartTest(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(testInt: Int): List<Question> {
        return repository.getStartTest(testInt)
    }
}