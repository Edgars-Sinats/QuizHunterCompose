package com.example.quizhuntercompose.feature_pickTest.domain.use_case

import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository

class GetMyQuestions (
        private val repository: QuestionRepository
    ) {
//    @Throws(InvalidQuestionException::class)
    suspend operator fun invoke(ids: List<Int>?, count: Int, nonAns: Boolean, wrongAns: Boolean): List<Question?> {
        return repository.getMyQuestions(ids, nonAns, wrongAns, count)
    }
}