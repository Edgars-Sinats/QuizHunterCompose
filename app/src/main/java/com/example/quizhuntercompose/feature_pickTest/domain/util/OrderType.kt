package com.example.quizhuntercompose.feature_pickTest.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
