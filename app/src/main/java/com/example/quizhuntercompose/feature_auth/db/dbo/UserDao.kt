package com.example.quizhuntercompose.feature_auth.db.dbo

import androidx.room.*
import com.example.quizhuntercompose.domain.model.UserKeys
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Query("SELECT Count(*) FROM User WHERE isPremium = 1")
    fun isUserPremium(): Flow<Boolean>

    @Query("SELECT Count(*) FROM User WHERE is_teacher = 1")
    fun isUserTeacher(): Flow<Boolean>

    @Query("SELECT Count(*) FROM User WHERE is_admin = 1")
    fun isUserAdmin(): Flow<Boolean>

    @Query("SELECT * FROM user_keys")
    fun getAllKeys(): Flow<List<UserKeys?>>

    @Query("SELECT test_key FROM user_keys WHERE test_id = :testId LIMIT 1")
    fun getKey(testId: Int): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserKeys(userKeys: List<UserKeys>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM User")
    suspend fun deleteUser()
}