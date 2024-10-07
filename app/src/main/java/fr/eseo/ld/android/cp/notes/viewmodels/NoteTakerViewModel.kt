package fr.eseo.ld.android.cp.notes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.eseo.ld.android.cp.notes.model.Note
import fr.eseo.ld.android.cp.notes.repository.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteTakerViewModel @Inject constructor(
    application: Application,
    private val repository: FirestoreRepository
) : AndroidViewModel(application) {


    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes : StateFlow<List<Note>> = _notes.asStateFlow()

    private val _note = MutableStateFlow<Note?>(null)
    val note : StateFlow<Note?> = _note.asStateFlow()



    fun addOrUpdate(note: Note) {
        repository.addOrUpdateNote(note)
        getNotes()
    }

    fun delete(noteId: String) {
        repository.delete(noteId)
        getNotes()
    }

    fun getNotes() {
        repository.getNotes { notes ->
            _notes.value = notes
        }
    }

    fun getNoteById(noteId: String) {
        _note.value = _notes.value.find { it.id == noteId }
    }


    fun deleteNote(noteId: String) {
        repository.delete(
            noteId
        )
        getNotes()
    }
}

