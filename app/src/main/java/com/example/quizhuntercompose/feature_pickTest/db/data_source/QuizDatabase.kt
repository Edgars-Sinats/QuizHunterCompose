package com.example.quizhuntercompose.feature_pickTest.db.data_source

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizhuntercompose.core_dbo.test.TestDao
import com.example.quizhuntercompose.core_dbo.test.TestEntity
import com.example.quizhuntercompose.core_dbo.user.UserKeysEntity
import com.example.quizhuntercompose.core_dbo.user.UserDao
import com.example.quizhuntercompose.core_dbo.user.UserEntity
import com.example.quizhuntercompose.feature_pickTest.domain.model.Question
import com.example.quizhuntercompose.feature_pickTest.domain.model.Topic
import javax.inject.Singleton



@Database( entities = [Question::class, Topic::class, TestEntity::class, UserEntity::class, UserKeysEntity::class], version = 4 )
abstract class QuizDatabase: RoomDatabase() {

    fun init(context: Context) {
        Log.i("QuizDatabase: ", " INIT DATABASE !")
//        getDatabase(context = context)
    }

    abstract val questionDao: QuestionDao
    abstract val userDao: UserDao
    abstract val testDao: TestDao

    companion   object {
        const val DATABASE_NAME = "HuntQuestion2"


        // Singleton prevents multiple instances of database opening at the
        // same time.  Volatile - writes on this thread made visible on other threads.
        @Volatile
//        @Provides
        @Singleton
        private var INSTANCE: QuizDatabase? = null

        //...getDatabase(, viewModelScope: CoroutineScope)
        fun getDatabase(context: Context): QuizDatabase =
            INSTANCE ?: synchronized(this) {
                Log.i("QuizDatabase: ", "After synchronized")
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }

            }

//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        QuizDatabase::class.java,
//                        DATABASE_NAME
//                    )

//                        .callback(StartingQuestions(context))
//                        .addCallback(StartingQuestions(context))
//                        .createFromAsset("db/huntQuestion.db") //huntQuestion.db
//                    .fallbackToDestructiveMigration()

//                        .build()
//                        .also { INSTANCE=it }
//                        .createOpenHelper().writableDatabase.execSQL(DROP)
//                    INSTANCE = instance

//                    Log.i("QuizDatabase","INSTANCE WAS NULL QUIZDATABASE? Now created." )


//                return instance


//                Log.i("QuizDatabase","is load only:" )
//                return@getDatabase instance
//                instance.questionDao.updateQuestion(context.assets.open("assets/").)



        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(
                appContext.applicationContext,
                QuizDatabase::class.java,
                DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(StartingQuestions(appContext))
                .build()
    }

//}

}



