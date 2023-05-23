package com.example.quizhuntercompose.core_dbo.test

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {

    @Query("SELECT * FROM test_table")
    fun getTests(): Flow<List<TestEntity>>

    @Query("SELECT * FROM test_table WHERE language = :language")
    fun getTestsLanguage(language: String): Flow<List<TestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(tests: TestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTests(tests: List<TestEntity>)

    @Query("SELECT * FROM test_table where test_id = :testId  LIMIT 1")
    fun getTestById(testId: Int): Flow<TestEntity?>

    //Make change - need synchronize with firebase
    @Query("UPDATE test_table SET is_favorite = :isFavorite WHERE test_id = :testId")
    fun updateIsFavorite(isFavorite: Boolean, testId: Int)

    @Query("DELETE FROM test_table")
    suspend fun removeAllTests()


}