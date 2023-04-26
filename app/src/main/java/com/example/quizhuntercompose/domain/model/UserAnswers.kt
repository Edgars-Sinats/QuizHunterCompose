package com.example.quizhuntercompose.domain.model

/**
 *
 * [UserAnswers] - user answers list of one test, should be uploaded and downloaded as map <testID, [UserAnswers]> in one document
 * [answers] - Map < [questionID] , [TestAnswers]>
 *
 */
data class UserAnswers(
    val questionId: String = "",
//    val testName: String = "", //TODO add in question table
    val answers: Map<Int, TestAnswer> = mapOf()
)
/**
 * [answerId]   - answerId same as question ID from Questions
 * [correctAnswers] - Correctly answered questions
 * [wrongAnswers] - Wrongly answered questions
 * [nonAnswers] - not answered questions.
 * [averageTimeSec] - average user answer time for question
 */
data class TestAnswer(
    val answerId: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val nonAnswers: Int,
    val averageTimeSec: Int
)
