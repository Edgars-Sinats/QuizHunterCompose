package com.example.quizhuntercompose.feature_auth.db.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quizhuntercompose.domain.model.QuizHunterUser
import java.security.Key


import java.util.*

@Entity(
    tableName = "user"
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_uid") val userUid: String,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "isPremium") val isPremium: Boolean = false,
    @ColumnInfo(name = "is_teacher") val isTeacher: Boolean = false,
    @ColumnInfo(name = "is_admin") val isAdmin: Boolean = false
//    @ColumnInfo(name = "test_keys") val testKeys: Map<Int, String>
)

//fun UserEntity.toDashCoinUser(): QuizHunterUser {
//    return QuizHunterUser(
//        userUid = userUid,
//        userName = userName,
//        email = email,
//        image = image,
//        premium = isPremium,
//        teacher = isTeacher,
//        admin = isAdmin
////        testKeys = mapOf()
//    )
//}

fun QuizHunterUser.toUserEntity(): UserEntity {
    return UserEntity(
        userUid = userUid ?: UUID.randomUUID().toString(),
        userName = userName,
        email = email,
        image = image,
        isPremium = premium,
        isTeacher = teacher,
        isAdmin = admin

    )
}
