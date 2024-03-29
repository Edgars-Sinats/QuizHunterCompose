package com.example.quizhuntercompose.feature_auth.presentation_profile

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizhuntercompose.cor.util.*
import com.example.quizhuntercompose.cor.util.Response.Success
import com.example.quizhuntercompose.core_state.UserState
import com.example.quizhuntercompose.core_usecases.QuizHunterUseCases
import com.example.quizhuntercompose.domain.model.QuizHunterUser
import com.example.quizhuntercompose.feature_auth.domain.*

import com.example.quizhuntercompose.feature_auth.presentation_profile.state.UpdatePictureState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val STATE_KEY_SELECTED_LANGUAGE = "profile.state.query.selected_language"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository,
    private val firebaseRepository: AuthFirebaseRepository,
    private val quizHunterUseCases: QuizHunterUseCases,
    private val quizHunterRepository: QuizHunterRepository,
    private val savedStateHandle: SavedStateHandle,
    ): ViewModel() {

    val selectedLanguage: MutableState<TestLanguages?> = mutableStateOf(null)

    private val _userCredential = MutableStateFlow(QuizHunterUser())
    val userCredential = _userCredential.asStateFlow()

    private val _authState = mutableStateOf<UserState>(UserState.UnauthedUser)
    val authState: State<UserState> = _authState

    private val _updateProfilePictureState = MutableStateFlow(UpdatePictureState())
    val updateProfilePictureState = _updateProfilePictureState.asStateFlow()

//    val displayName2 get() = firebaseRepository.isUserExist()
    val displayName get() = repo.displayName
    val photoUrl get() = repo.photoUrl

//    val aUser = quizHunterRepository.getQuizHunterUser()

    var signOutResponse by mutableStateOf<SignOutResponse>(Success(false))
        private set
    var revokeAccessResponse by mutableStateOf<RevokeAccessResponse>(Success(false))
        private set

    init {
        cacheQuizHunterUser()
        updateUiState()
        Log.i(TAG, "init 2")

        savedStateHandle.get<TestLanguages>(STATE_KEY_SELECTED_LANGUAGE)?.let { c ->
            setSelectedLanguage(c)
        }
        if(selectedLanguage.value == null){
            //Set up first language as default selected. //TODO add default from system default
            setSelectedLanguage( getTestLanguage(getAllTestLanguages()[1].value ) )
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            quizHunterUseCases.signOut()
            updateUiState()
            _userCredential.value = QuizHunterUser()
        }
    }

    fun getUserCredential() {
        viewModelScope.launch(Dispatchers.IO) {
            quizHunterRepository.getQuizHunterUser().collect{
                _userCredential.value = it ?: QuizHunterUser()
            }
        }
    }

    private fun cacheQuizHunterUser(){
        viewModelScope.launch(Dispatchers.IO){
            quizHunterRepository.getQuizHunterUser().collect() { user ->
                if (user != null) {
                    return@collect
                }

                firebaseRepository.getUserCredentials().collect() {
                    when(it) {
                        is Resource.Success -> it.data?.run { quizHunterRepository.saveQuizHunterUser(this) }
                        else -> {}
                    }
                }
            }
        }
    }

    fun updateUiState() {
        viewModelScope.launch {
            quizHunterUseCases.userStateProvider(
                function = {}
            ).collect { userState ->
                Log.i(TAG, "_authState: ${userState}")
                _authState.value = userState
            }
        }
    }

    fun updateProfilePicture(bitmap: Bitmap) {
        val imageName = userCredential.value.userUid ?: UUID.randomUUID().toString()

        uploadProfilePicture(
            imageName = imageName,
            bitmap = bitmap
        )
    }

    fun clearUpdateProfilePictureState() {
        _updateProfilePictureState.value = UpdatePictureState()
    }

    private fun uploadProfilePicture(imageName: String, bitmap: Bitmap) {
        viewModelScope.launch {
            firebaseRepository.uploadImageToCloud(
                name = imageName,
                bitmap = bitmap
            ).collect { uploadResult ->
                when(uploadResult) {
                    is Resource.Loading -> {
                        _updateProfilePictureState.value = UpdatePictureState(isLoading = true)
                    }
                    is Resource.Success -> {
                        val imageUrl = uploadResult.data ?: ""
                        updateProfilePicture(imageUrl = imageUrl)
                    }
                    is Resource.Error -> {
                        _updateProfilePictureState.value = UpdatePictureState(isFailure = true)
                    }
                }
            }
        }
    }

    private fun updateProfilePicture(imageUrl: String) {
        viewModelScope.launch {
            firebaseRepository
                .updateUserProfilePicture(imageUrl = imageUrl)
                .collect { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _updateProfilePictureState.value = UpdatePictureState(isLoading = true)
                        }
                        is Resource.Success -> {
                            _updateProfilePictureState.value = UpdatePictureState(isSuccess = true)
                        }
                        is Resource.Error -> {
                            _updateProfilePictureState.value = UpdatePictureState(isFailure = true)
                        }
                    }
                }
        }
    }

    fun isUserPremium(): Flow<Boolean> {
        return quizHunterRepository.isUserPremiumLocal()
    }

    fun updateUserLanguages(languages: String){

        _userCredential.value = _userCredential.value.copy(languages = languages)

        viewModelScope.launch {
            quizHunterRepository.saveQuizHunterUser(_userCredential.value)
        }
    }

    fun revokeAccess() = viewModelScope.launch {
        revokeAccessResponse = Response.Loading
        revokeAccessResponse = repo.revokeAccess()
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    fun onSelectedLanguageChanged(language: String) {
        val newLanguage = getTestLanguage(language)
        setSelectedLanguage(newLanguage)
//        onQueryChanged(language)
    }

    private fun setSelectedLanguage(language: TestLanguages?){
        selectedLanguage.value = language
        savedStateHandle.set(STATE_KEY_SELECTED_LANGUAGE, language)
    }

    private fun clearSelectedCategory() {
        setSelectedLanguage(null)
        selectedLanguage.value = null
    }
}