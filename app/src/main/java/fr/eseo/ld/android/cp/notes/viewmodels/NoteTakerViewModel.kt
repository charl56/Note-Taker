package fr.eseo.ld.android.cp.notes.viewmodels

import fr.eseo.ld.android.cp.notes.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteTakerViewModel {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes : StateFlow<List<Note>> = _notes.asStateFlow()

    private val _note = MutableStateFlow<Note?>(null)
    val note : StateFlow<Note?> = _note.asStateFlow()

    fun addOrUpdate(note : Note) {
        _notes.value =
            if (_notes.value.any { it.id == note.id }) {
                _notes.value.map { existingNote ->
                    if (existingNote.id == note.id) note else existingNote
                }
            } else {
                _notes.value + note
            }
    }


    fun getNoteById(noteId : String) {
        _note.value = _notes.value.find {it.id == noteId}
    }
}