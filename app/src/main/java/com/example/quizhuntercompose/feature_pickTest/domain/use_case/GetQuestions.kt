package com.example.quizhuntercompose.feature_pickTest.domain.use_case

import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository

class GetQuestions (
    private val repository: QuestionRepository
    ) {
        suspend operator fun invoke(count: Int, testId: Int): List<Question?> {
            return repository.getXQuestions(count, testId)
        }
    }