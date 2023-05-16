package com.example.quizhuntercompose.feature_tests.presentation.components

import com.example.quizhuntercompose.domain.model.FirebaseTestDocument
import com.example.quizhuntercompose.domain.model.TestFirebase
import com.google.firebase.Timestamp

fun ExperimentalTests(): FirebaseTestDocument {

    val test1 = TestFirebase(
        testID = 0,
        testName = "VMD Mednieku teorijas jautājumi.",
        dateCreated = Timestamp(91231313, 12313),
        dateModified = Timestamp(0,0),
        testImageUrl = "",
        language = "latvian",
        testDescription = "Tests no \"Valsts meža dienests\" jautājumiem. Tests sastāv no 666 dažādiem jautājumiem. Testā iekļautas 10 dažādas tēmas. ",
        needKey = false,
        additionalInfo = "Šis tests nesatur pēdējos(jaunākos) VMD jautājumus, kā piemēram par tēmu - medicīna",
        testRank = 1,
        isFavorite = false
    )

//    val newTime: Timestamp = Date(2022, 11,2,2,2,2)
//    new Timestamp(new Date(yyyy, mm, dd).getTime() / 1000, 0)

    val test2 = TestFirebase(
        testID = 1,
        testName = "CSDD B kategorijas eksāmena jautājumi",
        dateCreated = Timestamp(121231, 12312312),
        dateModified = Timestamp(3000000, 0),
        testImageUrl = null,
        language = "latvian",
        testDescription = "Jautājumi no CSDD B kategorijas teorijas. / Patreiz nav jautājumu.",
        needKey = false,
        additionalInfo = "No images yet",
        testRank = 1,
        isFavorite = false
    )

    val test3 = TestFirebase(
        testID = 1,
        testName = "Latīņu valodas frāzes",
        dateCreated = Timestamp(164231, 3600),
        dateModified = Timestamp(200000, 0),
        testImageUrl = null,
        language = "latvian",
        testDescription = "Izini seno latīņu valodas pamatfrāzes un teicienus.",
        needKey = false,
        additionalInfo = "",
        testRank = 1,
        isFavorite = false
    )

    val pairNr1 = mapOf(
        Pair("1", test1),
        Pair("2", test2),
        Pair("3", test3),
        Pair("4", test3),
        Pair("5", test3)
    )
    println("Returning upload object- FirebaseTestDocument")

    return FirebaseTestDocument(
        documentTests = (
                pairNr1
        )
    )

}