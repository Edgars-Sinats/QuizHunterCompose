package com.example.quizhuntercompose.feature_pickTest.domain.use_case

import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import com.example.quizhuntercompose.feature_pickTest.domain.repository.QuestionRepository

class GetAllTopics(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(): List<Topic?> {
        return repository.getAllTopics()
    }
}