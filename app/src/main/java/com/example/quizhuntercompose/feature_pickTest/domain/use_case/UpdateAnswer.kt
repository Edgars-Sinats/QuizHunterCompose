package com.example.quizhuntercompose.feature_pickTest.domain.use_case

import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository

class UpdateAnswer(
    private val repository: QuestionRepository
    ) {
    suspend operator fun invoke(question: Question) {
        repository.updateQuestion(question)
    }
}
