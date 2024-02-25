package com.example.quizhuntercompose.domain.model

import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

/**
 * [needKey] is boolean value for premium quiz,
 * if true it can be fully accessed only if user is Premium
 * or user has his own key for current [testID].
 *
 * [language] is quiz native language
 * [additionalInfo] is optional detailed info of test.
 * F.e. - Latvian official hunter licence test questions.
 * [dateModified] last time test was modified.
 */
//@Entity(tableName = "test_table")
data class Test(
    //TODO testId is Field name - we should remove duplicated data. Now Field ID (Map) and in fields (Map) is testID. ..See Firebase Schema.
    @PrimaryKey
    var testID: Int, //TODO rename QuestionDao table name with new one, and add new table?
    val testName: String,
    val testImageUrl: String? = "",
    val testDescription: String = "",
//    @ServerTimestamp
    val dateCreated: Long,
//    @ServerTimestamp
    val dateModified: Long,
    /**  Note. [Favorite] - only in Room */
    // TODO remove from Test?
    val isFavorite: Boolean,
    val needKey: Boolean,
    val additionalInfo: String? = "",
    //In room only is language. In Firebase DocId is language name...
    //TODO create function for new language Doc creation for Admins/Teachers.
    val language: String,
    val testRank: Int
//    val topics: List<Topic>,
//    val questions: List<Question>,
)

data class TestFirebase(
    //TODO testId is Field name - we should remove duplicated data. Now Field ID (Map) and in fields (Map) is testID. ..See Firebase Schema.
    @PrimaryKey
    val testID: Int = 0, //TODO rename QuestionDao table name with new one, and add new table?
    val testName: String = "WrongTest",
    val testImageUrl: String? = "",
    val testDescription: String = "",
//    @ServerTimestamp
    val dateCreated: Timestamp? = null,
    @ServerTimestamp
    val dateModified: Timestamp? = null,
    /**  Note. [Favorite] - only in Room */
    // TODO update state when getUserDataFromFirebase
    val isFavorite: Boolean = false,
    val needKey: Boolean = false,
    val additionalInfo: String? = "",
    //In room only is language. In Firebase DocId is language name...
    //TODO create function for new language Doc creation for Admins/Teachers.
    val language: String = "latvian",
    val testRank: Int = 1
//    val topics: List<Topic>,
//    val questions: List<Question>,
)

class   EntityTestMap(
    var doc: Map<String, Test>? = null
)

class FirebaseTestDocument(
    var documentTests: Map<String, TestFirebase>? = null
)

data class FirebaseTestDocument2 (
    var count: Map<String, TestFirebase> = emptyMap()
)

data class FirebaseTestDocument3 (
    var values: Map<String, FirebaseTestDocument4>
)

data class FirebaseTestDocument4 (
    var doc: TestFirebase
)


class FirebaseTestList (
    var test: List<Test>
)
