package com.example.quizhuntercompose.feature_pickTest.db.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic

@Dao
interface QuestionDao {

    //ELSE SHOULD BE IMPOSSIBLE
    @Query("SELECT COUNT (*) " +
            "FROM question_table " +
            "WHERE topic_id IN (:ids) " +
            "AND CASE " +
            "       WHEN :nonAns THEN (wrong_answers + correct_answers) = 0 " +
            "       WHEN :wrongAns THEN wrong_answers >= correct_answers " +
            "       WHEN :nonAns = 0 and :wrongAns = 0 THEN correct_answers >= 0 " +
            "       ELSE correct_answers + wrong_answers >= 0" +
            "   END "
            )
    fun getQuestionCountChecker(ids: List<Int>, nonAns: Boolean, wrongAns: Boolean) : Int

    // count - number of questions from DB.
    @Query("SELECT * " +
            "FROM question_table " +
            "WHERE topic_id IN (:ids) " +
            "AND CASE " +
            "       WHEN :nonAns THEN (wrong_answers + correct_answers) = 0 " +
            "       WHEN :wrongAns THEN wrong_answers >= correct_answers " +
            "       ELSE correct_answers + wrong_answers >= 0 " +
            "   END " +
            "ORDER BY random() LIMIT :count")
    fun getMyQuestions(count: Int, nonAns: Boolean, wrongAns: Boolean, ids: List<Int>?): List<Question> // asynchronous one-shot queries https://developer.android.com/training/data-storage/room/async-queries#one-shot


//    //UNION all nonAnswer > 0AND  wrong_answers > correct_answers // TRUE FALSE keywords are really just alternative spellings for the integer literals 1 and 0 respectively.
//    @Query("SELECT COUNT (*) " +
//            "FROM question_table " +
//            "WHERE CASE " +
//            "   WHEN :nonAns AND :wrongAns " +
//            "       THEN non_answers > 0 AND wrong_answers > correct_answers " +
//            "   WHEN :nonAns == 0 AND :wrongAns == 1 " +
//            "       THEN wrong_answers > correct_answers " +
//            "   WHEN :nonAns == 1 AND :wrongAns == 0 " +
//            "       THEN non_answers > 0 " +
//            "   END " +
//            "ORDER BY average_time_sec DESC " )
//    fun getQuestionCountOptions(ids: List<Int>, nonAns: Boolean, wrongAns: Boolean) : Int

    @Query("SELECT COUNT (*) " +
            "FROM question_table " +
            "WHERE topic_id = :id " +
            "AND (correct_answers > wrong_answers) ")
    fun getQuestionCount(id: Int): Int

    @Query("SELECT COUNT (*) " +
            "FROM question_table " +
            "WHERE topic_id IN (:ids) " +
            "AND non_answers <=  :noAns " +
            "AND wrong_answers > correct_answers " +
            "ORDER BY average_time_sec DESC " )
    fun getQuestionCountFrom(ids: List<Int>, noAns: Int): Int

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