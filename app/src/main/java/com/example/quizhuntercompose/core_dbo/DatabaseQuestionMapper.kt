package com.example.quizhuntercompose.core_dbo

import android.util.Log
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.domain.model.Test
import com.example.quizhuntercompose.feature_pickTest.domain.model.FirebaseQuestion
import com.example.quizhuntercompose.feature_pickTest.domain.model.FirebaseUserQuestionData
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question

object DatabaseQuestionMapper: QuestionMapper<List<Question>, List<FirebaseQuestion>, List<FirebaseUserQuestionData>, String> {

    override fun toDomain(entity: List<Question>): Pair<List<FirebaseQuestion>, List<FirebaseUserQuestionData>> {
        Log.i("DatabaseQuestionMapper", "toDomain start building")
        val firebaseQuestions = entity.map { data ->
            Log.i("DatabaseQuestionMapper", "entity: $data")
            FirebaseQuestion(
                questionID = data.questionID,
                question = data.question,
                answer1 = data.answer1,
                answer2 = data.answer2,
                answer3 = data.answer3,
                answer4 = data.answer4,
                correctAnswer = data.correctAnswer,
                topic = data.topic,
                explanation = data.explanation,
                imageUrl = data.imageUrl
            )
        }

        val firebaseUserQuestionsData  = entity.mapNotNull { data ->
            if (data.averageAnswerTime != 0 ){
                FirebaseUserQuestionData(
                    questionID = data.questionID,
                    testId = data.testID,
                    averageAnswerTime = data.averageAnswerTime,
                    lastAnswerTime = data.lastAnswerTime,
                    nonAnswers = data.nonAnswers,
                    wrongAnswers = data.wrongAnswers,
                    correctAnswers = data.correctAnswers
                )
            } else {
                null
            }
        }

        return Pair(firebaseQuestions,firebaseUserQuestionsData)
    }

    override fun toEntity(domain: Pair<List<FirebaseQuestion>, List<FirebaseUserQuestionData>>, testID: String): List<Question> {
        val firebaseQuestions = domain.first
        val firebaseUserQuestionData = domain.second

        return firebaseQuestions.mapIndexed { index, firebaseQuestion ->
            val firebaseUserQuestion = firebaseUserQuestionData.getOrNull(index)

            Question(
                questionID = firebaseQuestion.questionID,
                question = firebaseQuestion.question,
                answer1 = firebaseQuestion.answer1,
                answer2 = firebaseQuestion.answer2,
                answer3 = firebaseQuestion.answer3,
                answer4 = firebaseQuestion.answer4,
                correctAnswer = firebaseQuestion.correctAnswer,
                topic = firebaseQuestion.topic,
                explanation = firebaseQuestion.explanation,
                imageUrl = firebaseQuestion.imageUrl,
                testID = testID.toInt(),
                averageAnswerTime = firebaseUserQuestion?.averageAnswerTime ?: 0,
                lastAnswerTime = firebaseUserQuestion?.lastAnswerTime ?: 0,
                nonAnswers = firebaseUserQuestion?.nonAnswers ?: 0,
                wrongAnswers = firebaseUserQuestion?.wrongAnswers ?: 0,
                correctAnswers = firebaseUserQuestion?.correctAnswers ?: 0
            )
        }
    }

}

fun List<Question>.toDomainQuestion() = DatabaseQuestionMapper.toDomain(this)
fun (Pair<List<FirebaseQuestion>, List<FirebaseUserQuestionData>>).toEntityQuestion() = DatabaseQuestionMapper.toEntity(this, testID = String())