package fr.eseo.ld.android.cp.notes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.eseo.ld.android.cp.notes.model.Note
import fr.eseo.ld.android.cp.notes.model.data.NoteTakerDatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteTakerViewModel(application : Application) : AndroidViewModel(application) {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes : StateFlow<List<Note>> = _notes.asStateFlow()

    private val _note = MutableStateFlow<Note?>(null)
    val note : StateFlow<Note?> = _note.asStateFlow()

    private val db = NoteTakerDatabaseProvider.getDatabase(application)

    init {
        viewModelScope.launch(Dispatchers.IO){
            _notes.value = db.noteDao().getAllNotes()
        }
    }


    fun addOrUpdate(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            if(note.id.isBlank()) {
                db.noteDao().insert(note.copy(id=System.currentTimeMillis().toString()))
            } else {
                db.noteDao().update(note)
            }
        }
    }


    fun getNoteById(noteId : String) {
        viewModelScope.launch(Dispatchers.IO) {
            _note.value = db.noteDao().getNoteById(noteId)
        }
    }

    fun loadNotes() {
//        withContext(Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.value = db.noteDao().getAllNotes()
        }
    }

    fun deleteNote(noteId : String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.noteDao().deleteNoteById(noteId)
            _notes.value = db.noteDao().getAllNotes()
        }
    }
}


class NoteTakerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteTakerViewModel::class.java)){
            return NoteTakerViewModel(application) as T
        }
        throw IllegalArgumentException("Not correct VM")
    }
}
