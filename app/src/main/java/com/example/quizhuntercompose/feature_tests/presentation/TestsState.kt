package com.example.quizhuntercompose.feature_tests.presentation

import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.domain.model.Test

data class TestsState(
    val isLoading: Boolean = false,
    var tests: List<TestEntity> = emptyList(),
    val isEmpty: Boolean = false,
    val error: String = "",
    val openedTest: TestEntity? = null,
    val language: String? = null
)
