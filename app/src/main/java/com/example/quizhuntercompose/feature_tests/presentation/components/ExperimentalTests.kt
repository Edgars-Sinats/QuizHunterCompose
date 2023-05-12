package com.example.quizhuntercompose.feature_tests.presentation.components

import com.example.quizhuntercompose.domain.model.FirebaseTestDocument
import com.example.quizhuntercompose.domain.model.TestFirebase
import com.google.firebase.Timestamp

fun ExperimentalTests(): FirebaseTestDocument {

    val test1 = TestFirebase(
        testID = 1,
        testName = "Test 1",
        dateCreated = Timestamp(91231313, 12313),
        dateModified = null,
        testImageUrl = null,
        language = "latvian",
        testDescription = "Tests no \"Valsts meža dienests\" jautājumiem. Tests sastāv no 666 dažādiem jautājumiem. Testā iekļautas 10 dažādas tēmas. ",
        needKey = false,
        additionalInfo = null,
        testRank = 1,
        isFavorite = false
    )

//    val newTime: Timestamp = Date(2022, 11,2,2,2,2)
//    new Timestamp(new Date(yyyy, mm, dd).getTime() / 1000, 0)

    val test2 = TestFirebase(
        testID = 2,
        testName = "CSDD B kategorijas eksāmena jautājumi",
        dateCreated = Timestamp(121231, 12312312),
        dateModified = null,
        testImageUrl = null,
        language = "latvian",
        testDescription = "Jautājumi no CSDD B kategorijas teorijas. / Patreiz nav jautājumu.",
        needKey = false,
        additionalInfo = null,
        testRank = 1,
        isFavorite = false
    )

    val pairNr1 = mapOf(
        Pair("1", test1),
        Pair("2", test2),
        Pair("3", test2)
    )


    println("Returning upload object- FirebaseTestDocument")

    return FirebaseTestDocument(
        documentTests = (
                pairNr1
                //                mapOf(
//                    Pair(1, test1),
//                    Pair(2, test2),
//                    Pair(3, test2)
//                )
        )
    )

}