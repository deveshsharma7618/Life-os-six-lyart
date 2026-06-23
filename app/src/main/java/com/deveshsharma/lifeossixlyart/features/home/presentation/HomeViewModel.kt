package com.deveshsharma.lifeossixlyart.features.home.presentation

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val morningThoughts: List<Thought> = emptyList()
)

class HomeViewModel : ViewModel(){
    val db = Firebase.firestore

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMorningThoughts()
    }

    fun saveTodayMood(sharedPreferences: SharedPreferences, mood : String){
        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        sharedPreferences.edit{
            putString("today_mood", mood)
            putString("mood_date", currentDate)
        }
    }

    private val _moodState = MutableStateFlow<String?>(null)
    val moodState: StateFlow<String?> = _moodState.asStateFlow()

    fun loadMood(sharedPreferences: SharedPreferences) {
        val savedDate = sharedPreferences.getString("mood_date", null)
        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        if (savedDate == currentDate) {
            val mood = sharedPreferences.getString("today_mood", null)
            _moodState.value = mood
        } else {
            _moodState.value = null
        }
    }

    fun updateMood(sharedPreferences: SharedPreferences, mood: String) {
        _moodState.value = mood
        saveTodayMood(sharedPreferences, mood)
    }

    fun loadMorningThoughts(){
        val userId = Firebase.auth.currentUser?.uid
        _uiState.value = _uiState.value.copy(isLoading = true)
        db.collection(userId!!).document("MorningThoughts").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = exception.message)
                return@addSnapshotListener
            }

            val thoughts = snapshot?.get("thoughts") as? List<Thought> ?: emptyList()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                morningThoughts = thoughts
            )
        }
    }

    fun saveMorningThought(userId: String, thoughts: List<Thought>, newThought: String) {
        val updatedThoughts = thoughts + Thought(newThought)

        val thoughtMap = mutableMapOf(
            "thoughts" to updatedThoughts
        )

        db.collection(userId).document("MorningThoughts").set(
            thoughtMap
        ).addOnSuccessListener {
            Log.d("HomeViewModel", "Morning thought saved successfully")
        }.addOnFailureListener { e ->
            _uiState.value = _uiState.value.copy(error = e.message)
        }
    }

    fun deleteMorningThought(userId: String, thoughts: List<Thought>, thoughtToDelete: Thought) {
        val updatedThoughts = thoughts.filter { it != thoughtToDelete }
        val thoughtMap = mapOf("thoughts" to updatedThoughts)

        db.collection(userId).document("MorningThoughts").set(thoughtMap)
            .addOnSuccessListener {
                Log.d("HomeViewModel", "Morning thought deleted successfully")
            }.addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(error = e.message)
            }
    }

}

data class Thought(val thought : String, val day : String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE ))
