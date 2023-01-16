package com.example.quizhuntercompose.feature_pickTest.db.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic

@Dao
interface QuestionDao {

    @Query("SELECT COUNT (*) " +
            "FROM question_table " +
            "WHERE topic_id = :id " +
            "AND (correct_answers > wrong_answers) ")
    fun getQuestionCount(id: Int): Int

    @Query("SELECT * FROM question_table") //ORDER BY question_id ASC
    fun getAllQuestions(): List<Question>

    //TODO change 3 => 60 once limit implemented
    @Query("SELECT * FROM question_table ORDER BY random() LIMIT 3")
    fun getStartTest(): List<Question>

    // count - number of questions from DB.
    @Query("SELECT * FROM question_table ORDER BY random() LIMIT :count")
    fun getQuestionsX(count: Int): List<Question> // asynchronous one-shot queries https://developer.android.com/training/data-storage/room/async-queries#one-shot

    // count - number of questions from DB.
    @Query("SELECT * FROM question_table WHERE question_id =  :id ")
    fun getQuestionX(id: Int): Question

    @Query("SELECT * FROM question_table WHERE topic_id = :topic LIMIT :count ")
    fun getXFromTopic(topic : String, count: Int): List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateQuestion(question: Question)

    @Query("SELECT * FROM topic_table")
    fun getAllTopic(): List<Topic>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTopics(topics: List<Topic>)

//    @Query("SELECT question FROM question_table")
//    suspend fun callQuestion(): List<String>
}