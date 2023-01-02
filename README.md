Question app
=================================

Single player game app that displays scrambled words. To play the game, player has to make a
word using all the letters in the displayed scrambled word.
Question app what will help to learn new topic with answering on question and choosing/filtering already positive(mostly) answered questions. 
    //Next step implement theory for questions in case of explanation needed. [// 1] Plan of taking text block of theory and linking to questions with help of AI text recognition tool. 
This code demonstrates the Android Architecture pattern MVVM and component like - ViewModel and StateFlow.

Created database from .pdf of Hunter questions and implemented them with JSON objects with.addCallback() as in database builder .readFromFile() did`t work with .db or json.
Next step read data from Firebase Firestore just like in previous project https://github.com/Edgars-Sinats/LumiBricks

Getting Started - issues
---------------
1. In case of first run you got the error, run  the second time.


### App
Currently implemented plain quiz with 3 questions to make sure .db is linked correctly. ```_quiz_test``` folder under```feature_pickTest/presentation``` represent only feature.

- __quiz_test__: This is responsible for taken test.
- - __pick_test__: This is responsible for choosing test. //```_TODO``` 

