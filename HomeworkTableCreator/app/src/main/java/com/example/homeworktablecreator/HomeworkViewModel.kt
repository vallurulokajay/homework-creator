package com.example.homeworktablecreator

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeworkViewModel : ViewModel() {

    // State for the single date selection
    var selectedDate by mutableStateOf<Long?>(null)
        private set

    // State for the list of homework entries
    private val _entries = mutableStateListOf<HomeworkEntry>()
    val entries: List<HomeworkEntry> = _entries

    // State for the generated image URI (for preview and sharing)
    var generatedImageUri by mutableStateOf<Uri?>(null)
        private set
    
    // State for UI messages (errors, success)
    var uiMessage by mutableStateOf<String?>(null)
        private set

    fun setDate(dateMillis: Long) {
        selectedDate = dateMillis
    }

    fun addEntry(subject: String, homework: String) {
        if (subject.isBlank() || homework.isBlank()) {
            uiMessage = "Subject and Homework cannot be empty."
            return
        }
        _entries.add(HomeworkEntry(subject = subject.trim(), homework = homework.trim()))
    }

    fun removeEntry(entry: HomeworkEntry) {
        _entries.remove(entry)
    }

    fun updateEntry(entry: HomeworkEntry, newSubject: String, newHomework: String) {
        val index = _entries.indexOfFirst { it.id == entry.id }
        if (index != -1) {
            _entries[index] = entry.copy(subject = newSubject.trim(), homework = newHomework.trim())
        }
    }

    fun setGeneratedUri(uri: Uri) {
        generatedImageUri = uri
    }
    
    fun clearUiMessage() {
        uiMessage = null
    }
}
