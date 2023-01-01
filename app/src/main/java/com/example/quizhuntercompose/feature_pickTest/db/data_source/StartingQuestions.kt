package com.example.quizhuntercompose.feature_pickTest.db.data_source

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
//import timber.log.Timber


class StartingQuestions (private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            fillWithStartingNotes(context)
        }
    }

    private suspend fun fillWithStartingNotes(context: Context) {
        val dao = QuizDatabase.getDatabase(context).questionDao

        // using try catch to load the necessary data
        try {
            //creating variable that holds the loaded data
            val questionN = loadJSONArray(context)
            if (questionN != null){
                //looping through the variable as specified fields are loaded with data
                for (i in 0 until questionN.length()){
                    //variable to obtain the JSON object
                    val item = questionN.getJSONObject(i)
                    //Using the JSON object to assign data
                    val answer1 = item.getString("answer_1")
                    val answer2 = item.getString("answer_2")
                    val answer3 = item.getString("answer_3")
                    val answer4 = item.getString("answer_4")

                    val averageTimeSec1 = item.getInt("average_time_sec")
                    val correctAnswer1 = item.getInt("correct_answer")
                    val correctAnswers1 = item.getInt("correct_answers")
                    val lastTimeSec1 = item.getInt("last_time_sec")
                    val nonAnswers1 = item.getInt("non_answers")
                    val question = item.getString("question")
                    val questionId = item.getInt("question_id")
                    val topicId = item.getInt("topic_id")
                    val wrongAnswers = item.getInt("wrong_answers")

                    Log.d("StartingQuestion: ","questionLoaded: $questionId")


                    //data loaded to the entity
                    val noteEntity = Question(
                        answer2 = answer2,
                        answer1 = answer1,
                        answer3 = answer3,
                        answer4 = answer4,
                        averageAnswerTime = averageTimeSec1,
                        correctAnswer = correctAnswer1,
                        correctAnswers = correctAnswers1,
                        lastAnswerTime = lastTimeSec1,
                        nonAnswers = nonAnswers1,
                        question = question,
                        questionID = questionId,
                        topic = topicId,
                        wrongAnswers = wrongAnswers
                                            )

                    //using dao to insert data to the database
                    dao.updateQuestion(noteEntity)
                    Log.d("StartingQuestion: ","questionUpdated: $questionId")
                }
            }
        }
        //error when exception occurs
        catch (e: JSONException) {
            Log.d("StartingQuestion: ","fillWithStartingNotes exception: $e")
        }
    }



    // loads JSON data
    private fun loadJSONArray(context: Context): JSONArray?{
        //obtain input byte
        val inputStream = context.assets.open("question_table.json") //.apenRawResource(R.raw.notes)
        //using Buffered reader to read the inputstream byte
        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }
}