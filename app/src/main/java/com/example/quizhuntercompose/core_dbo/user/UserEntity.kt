package com.example.quizhuntercompose.core_dbo.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quizhuntercompose.domain.model.QuizHunterUser

@Entity(
    tableName = "user_table"
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_uid") val userUid: String,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "isPremium") val isPremium: Boolean? = false,
    @ColumnInfo(name = "is_teacher") val isTeacher: Boolean? = false,
    @ColumnInfo(name = "is_admin") val isAdmin: Boolean? = false,
    @ColumnInfo(name = "languages") val languages: String?,
//    @ColumnInfo(name = "test_keys") val testKeys: Map<String, String>
)

fun UserEntity.toQuizHunterUser(): QuizHunterUser {
    return QuizHunterUser(
        userUid = userUid,
        userName = userName,
        name = name,
        email = email,
        image = image,
        premium = isPremium,
        teacher = isTeacher,
        admin = isAdmin,
        languages = languages
//        testKeys = mapOf()
    )
}

fun QuizHunterUser.toUserEntity(): UserEntity {
    return UserEntity(
        userUid = userUid,
        userName = userName,
        name = name,
        email = email,
        image = image,
        isPremium = premium,
        isTeacher = teacher,
        isAdmin = admin,
        languages = languages

    )
}
