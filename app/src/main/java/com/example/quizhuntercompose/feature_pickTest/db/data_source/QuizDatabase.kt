package com.example.quizhuntercompose.feature_pickTest.db.data_source

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import javax.inject.Singleton



@Database( entities = [Question::class, Topic::class], version = 1 )
abstract class QuizDatabase: RoomDatabase() {
//    fun init(context: Context) {
//        getDatabase(context = context)
//    }

    abstract val questionDao: QuestionDao

    companion object {
        const val DATABASE_NAME = "HuntQuestion2"



        //    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time. todo why volatile
        @Volatile
//        @Provides
        @Singleton
        private var INSTANCE :  QuizDatabase? = null
        //...getDatabase(, viewModelScope: CoroutineScope)
        fun getDatabase(context: Context): QuizDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        QuizDatabase::class.java,
                        DATABASE_NAME
                    )
                        .addCallback(StartingQuestions(context))
//                        .createFromAsset("db/huntQuestion.db") //huntQuestion.db
//                    .fallbackToDestructiveMigration()

                        .build()
//                        .createOpenHelper().writableDatabase.execSQL(DROP)

                    INSTANCE = instance
                }
                return instance


                Log.i("QuizDatabase","is load only:" )
                // return instance
//                return@getDatabase instance
//                instance.questionDao.updateQuestion(context.assets.open("assets/").)
            }
        }
    }
//}

}



