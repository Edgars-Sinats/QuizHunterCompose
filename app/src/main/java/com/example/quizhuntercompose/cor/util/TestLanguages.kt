package com.example.quizhuntercompose.cor.util
import com.example.quizhuntercompose.cor.util.TestLanguages.*

enum class TestLanguages(val value: String){
    LATVIAN("latvian"),
    ENGLISH("english"),
    RUSSIAN("Russian"),
    ESTONIAN("Estonian"),
    FRENCH("French"),
    DUTCH("Dutch"),
    GERMAN("German")
}

fun getAllTestLanguages(): List<TestLanguages>{
    return listOf(LATVIAN, ENGLISH, RUSSIAN, ESTONIAN, FRENCH, DUTCH, GERMAN)
}

fun getTestLanguage(value: String): TestLanguages? {
    val map = TestLanguages.values().associateBy(TestLanguages::value)
    return map[value]
}
