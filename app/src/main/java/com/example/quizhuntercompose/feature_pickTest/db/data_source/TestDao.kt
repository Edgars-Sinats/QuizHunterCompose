package com.example.quizhuntercompose.feature_pickTest.db.data_source

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizhuntercompose.feature_pickTest.domain.model.Test

//TODO 1 !!! Update question_table to implement multiple test with testIds - Limit
interface TestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: Test)

//    @Query("SELECT * FROM question_table WHERE test_id = :testId")
//    fun getTest(testId: Int): Test

}