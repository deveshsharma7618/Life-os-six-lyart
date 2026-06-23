package com.deveshsharma.lifeossixlyart.features.diary.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DiaryUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val diaryList: List<Diary> = emptyList()
)
data class Diary(
    val title: String,
    val desc : String,
    val date : String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE )
)

class DiaryViewModel : ViewModel(){
    val db = Firebase.firestore

    private val _uiState = MutableStateFlow(DiaryUIState())
    val uiState: StateFlow<DiaryUIState> = _uiState.asStateFlow()

    init {
        loadDiaries()
    }


    fun loadDiaries(){
        val userId = Firebase.auth.currentUser?.uid
        if (userId == null) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "User not authenticated")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)
        db.collection(userId).document("Diaries").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = exception.message)
                return@addSnapshotListener
            }

            val diaryList = snapshot?.get("dairies") as? List<Map<String, Any>> ?: emptyList()
            val diaries = diaryList.map {
                Diary(it["title"] as? String ?: "", it["desc"] as? String ?: "", it["date"] as? String ?: "")
            }
            Log.d("XYZ", diaryList.toString())
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                diaryList = diaries
            )
        }
    }

    fun saveDiary(title: String, desc: String) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val currentDiaries = _uiState.value.diaryList
        val newDiary = Diary(title = title, desc = desc)
        saveDiary(userId, currentDiaries, newDiary)
    }

    fun saveDiary(userId: String, diaries: List<Diary>, newDiary: Diary) {
        val updatedDiaries = diaries + newDiary

        val diaryMap = mapOf(
            "dairies" to updatedDiaries
        )

        db.collection(userId).document("Diaries").set(
            diaryMap
        ).addOnSuccessListener {
                _uiState.value = _uiState.value.copy(diaryList = updatedDiaries)
            Log.d("DiaryViewModel", "Diary saved successfully")
        }.addOnFailureListener { e ->
            _uiState.value = _uiState.value.copy(error = e.message)
        }
    }

    fun deleteDiary(diary: Diary) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val currentDiaries = _uiState.value.diaryList
        deleteDiary(userId, currentDiaries, diary)
    }

    fun deleteDiary(userId: String, diaries: List<Diary>, diaryToDelete: Diary) {
        val updatedDiaries = diaries.filter { it != diaryToDelete }
        val diaryMap = mapOf("dairies" to updatedDiaries)

        db.collection(userId).document("Diaries").set(diaryMap)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(diaryList = updatedDiaries)
                Log.d("DiaryViewModel", "Diary deleted successfully")
            }.addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(error = e.message)
            }
    }
}