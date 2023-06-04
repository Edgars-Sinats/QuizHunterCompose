package com.example.quizhuntercompose.feature_pickTest.db.data_source

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import kotlinx.coroutines.*
import okhttp3.internal.wait
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
//import timber.log.Timber


class StartingQuestions (private val context: Context) : RoomDatabase.Callback() {
    //tODO 1.____ START HERE Does we load question in db.
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            fillWithStartingDatabase(context)
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
    }

    private suspend fun fillWithStartingDatabase(context: Context) {
        val dao = QuizDatabase.getDatabase(context).questionDao

        // using try catch to load the necessary data
        try {
            //creating variable that holds the loaded data
            val questionN = loadJSONArray(context)
            val topicN = loadJSONArrayTopics(context)
            val questionLatinLatvian = loadJSONArrayGptLatinLatvian(context)
            val questionUniverseEnglish = loadJSONArrayGptUniverseEnglish(context)
            val questionCSDDLatvian = loadJSONArrayCSDDLatvian(context)

            checkIfNullOrCreate(questionN, dao)
            checkIfNullOrCreate(questionLatinLatvian, dao)
            checkIfNullOrCreate(questionUniverseEnglish, dao)
            checkIfNullOrCreate(questionCSDDLatvian, dao)

//            if (questionN != null){
//                //looping through the variable as specified fields are loaded with data
//                for (i in 0 until questionN.length()){
//                    //variable to obtain the JSON object
//                    val item = questionN.getJSONObject(i)
//                    //Using the JSON object to assign data
//                    val answer1 = item.getString("answer_1")
//                    val answer2 = item.getString("answer_2")
//                    val answer3 = item.getString("answer_3")
//                    val answer4 = item.getString("answer_4")
//
//                    val averageTimeSec1 = item.getInt("average_time_sec")
//                    val correctAnswer1 = item.getInt("correct_answer")
//                    val correctAnswers1 = item.getInt("correct_answers")
//                    val lastTimeSec1 = item.getInt("last_time_sec")
//                    val nonAnswers1 = item.getInt("non_answers")
//                    val question = item.getString("question")
//                    val questionId = item.getInt("question_id")
//                    val testId = item.getInt("test_id")
//                    val topicId = item.getInt("topic_id")
//                    val wrongAnswers = item.getInt("wrong_answers")
//
//                    Log.d("StartingQuestion: ","questionLoaded: $questionId")
//
//                    //data loaded to the entity
//                    val noteEntity = Question(
//                        answer2 = answer2,
//                        answer1 = answer1,
//                        answer3 = answer3,
//                        answer4 = answer4,
//                        averageAnswerTime = averageTimeSec1,
//                        correctAnswer = correctAnswer1,
//                        correctAnswers = correctAnswers1,
//                        lastAnswerTime = lastTimeSec1,
//                        nonAnswers = nonAnswers1,
//                        explanation = null,
//                        question = question,
//                        questionID = questionId,
//                        topic = topicId,
//                        wrongAnswers = wrongAnswers,
//                        testID = testId
//                    )
//
//                    //using dao to insert data to the database
//                    dao.updateQuestion(noteEntity)
//                    Log.d("StartingQuestion: ","questionUpdated: $questionId")
//                }
//            }

            // Update topics
            if (topicN != null) {
                val listTopics: List<Topic> = emptyList()
                val topicMutableList = listTopics.toMutableList()
                for (i in 0 until topicN.length()){
                    val item = topicN.getJSONObject(i)
                    val topicId = item.getInt("topic_id")
                    val testId = item.getInt("test_id")
                    val topicName = item.getString("topic")

                    val topicEntity = Topic(
                        topic = topicName,
                        topicId = topicId,
                        testId = testId //TODO Update Topic table, create testTable
                    )
                    topicMutableList.add(topicEntity)
                }
                dao.updateTopics(topicMutableList)
            }

        }
        //error when exception occurs
        catch (e: JSONException) {
            Log.d("StartingQuestion: ","fillWithStartingNotes exception: $e")
        }
    }

    private fun checkIfNullOrCreate(json: JSONArray?, questionDao: QuestionDao){
        if (json != null){
            //looping through the variable as specified fields are loaded with data
            for (i in 0 until json.length()){
                //variable to obtain the JSON object
                val item = json.getJSONObject(i)
                //Using the JSON object to assign data
                val answer1 = item.getString("answer_1")
                val answer2 = item.getString("answer_2")
                val answer3 = item.getString("answer_3")
                val answer4 = item.getString("answer_4")

                val averageTimeSec1 = item.getInt("average_time_sec")
                val correctAnswer1 = item.getInt("correct_answer")
                val correctAnswers1 = item.getInt("correct_answers")
                val imageUrl1 = item.getString("imageUrl")
                val lastTimeSec1 = item.getInt("last_time_sec")
                val nonAnswers1 = item.getInt("non_answers")
                val question = item.getString("question")
                val questionId = item.getInt("question_id")
                val testId = item.getInt("test_id")
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
                    explanation = null, //TODO
                    question = question,
                    questionID = questionId,
                    topic = topicId,
                    wrongAnswers = wrongAnswers,
                    testID = testId,
                    imageUrl = imageUrl1
                )

                //using dao to insert data to the database
                questionDao.updateQuestion(noteEntity)
                Log.d("StartingQuestion: ","questionUpdated: $questionId")
            }
        } else {
            Log.i("StartingQuestion", "Json was null for ")
        }
    }


    // loads JSON data
    private fun loadJSONArray(context: Context): JSONArray?{
        Log.d("StartingQuestion: ","loadJsonQuestions")

        //obtain input byte
        val inputStream = context.assets.open("question_table.json") //.apenRawResource(R.raw.notes)
        //using Buffered reader to read the inputstream byte
        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }

    private fun loadJSONArrayTopics(context: Context): JSONArray?{
        Log.d("StartingQuestion: ","loadJsonTopics")
        val inputStream = context.assets.open("topic_table.json")
        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }

    private fun loadJSONArrayGptUniverseEnglish(context: Context): JSONArray?{
        Log.d("StartingQuestion: ","loadJsonGptUniverse")
        val inputStream = context.assets.open("gpt_universe_english.json")
        BufferedReader(inputStream.reader()).use {
            Log.i(TAG, "gpt_universe_english returning")
            return JSONArray(it.readText())
        }
    }

    private fun loadJSONArrayCSDDLatvian(context: Context): JSONArray?{
        Log.d("StartingQuestion: ","loadJsonGptUniverse")
        val inputStream = context.assets.open("questions_csdd_b_latvian.json")
        BufferedReader(inputStream.reader()).use {
            Log.i(TAG, "questions_csdd_b_latvian returning")
            return JSONArray(it.readText())
        }
    }

    private fun loadJSONArrayGptLatinLatvian(context: Context): JSONArray?{
        Log.d(TAG,"loadJsonGptUniverse")
        val inputStream = context.assets.open("gpt_questions_latin_latvian.json")
        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }
}

private const val TAG = "StartingQuestion"