package com.example.quizhuntercompose.core_dbo.user

import androidx.room.*
import dagger.Component
import dagger.hilt.android.internal.modules.ApplicationContextModule
import kotlinx.coroutines.flow.Flow


@Component(dependencies = [ApplicationContextModule::class])
@Dao
interface UserDao {

    @Query("SELECT * FROM user_table LIMIT 1")
    fun getUser(): Flow<UserEntity>

    @Query("SELECT Count(*) FROM user_table WHERE isPremium = 1")
    fun isUserPremium(): Flow<Boolean>

    @Query("SELECT Count(*) FROM user_table WHERE is_teacher = 1")
    fun isUserTeacher(): Flow<Boolean>

    @Query("SELECT Count(*) FROM user_table WHERE is_admin = 1")
    fun isUserAdmin(): Flow<Boolean>

    @Query("SELECT * FROM user_keys")
    fun getAllKeys(): Flow<List<UserKeysEntity?>>

    @Query("SELECT test_key FROM user_keys WHERE test_id = :testId LIMIT 1")
    fun getKey(testId: Int): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserKeys(userKeyEntities: List<UserKeysEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM user_table")
    suspend fun deleteUser()
}