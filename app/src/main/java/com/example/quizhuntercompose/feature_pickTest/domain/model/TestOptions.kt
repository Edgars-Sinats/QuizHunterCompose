package com.example.quizhuntercompose.feature_pickTest.domain.model

data class TestOptions(
    val ids: List<Int>,
    val count : Int,
    val nonAns: Boolean,
    val wrongAns: Boolean
)
