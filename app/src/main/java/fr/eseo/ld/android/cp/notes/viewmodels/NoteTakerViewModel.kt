package fr.eseo.ld.android.cp.notes.viewmodels

import androidx.lifecycle.ViewModel
import fr.eseo.ld.android.cp.notes.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class NoteTakerViewModel : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes : StateFlow<List<Note>> = _notes.asStateFlow()

    private val _note = MutableStateFlow<Note?>(null)
    val note : StateFlow<Note?> = _note.asStateFlow()


    init {
        initializeDummyNotes()
    }

    private fun initializeDummyNotes() {
        val dummyNotes= listOf(
            Note(
                id ="1",
                title ="My first note",
                body = "Not very interesting",
                author = "Bob",
                creationDate = Date(),
                modificationDate = Date()
            ),
            Note(
                id ="2",
                title ="My second note",
                body = "Not very interesting\n\but\n a lot longer\nthan other notes.",
                author = "Bob",
                creationDate = Date(),
                modificationDate = Date()
            ),
            Note(
                id ="3",
                title ="My second note",
                body = "Not very interesting\n\but\n a lot longer\nthan other notes.",
                author = "Bob",
                creationDate = Date(),
                modificationDate = Date()
            ),
            Note(
                id ="4",
                title ="My Biggest note",
                body = "Not very interesting\n\but\n a lot longer\nthan other notes\nfrdr\nfrfrf\nfrfrf\nnnfrfr.",
                author = "Bob",
                creationDate = Date(),
                modificationDate = Date()
            ),
            Note(
                id ="5",
                title ="My second note",
                body = "Not very interesting\n\but\n a lot longer\nthan other notes.",
                author = "Bob",
                creationDate = Date(),
                modificationDate = Date()
            ),
            Note(
                id ="6",
                title ="My second note",
                body = "Not very interesting\n\but\n a lot longer\nthan other notes.",
                author = "Bob",
                creationDate = Date(),
                modificationDate = Date()
            ),
            Note(
                id ="7",
                title ="My second note",
                body = "Not very interesting\n\but\n a lot longer\nthan other notes.",
                author = "Bob",
                creationDate = Date(),
                modificationDate = Date()
            )
        )
        _notes.value = dummyNotes
    }

    fun addOrUpdate(note: Note) {
        _notes.value = if (_notes.value.any { it.id == note.id }) {
            _notes.value.map { if (it.id == note.id) note else it }
        } else {
            _notes.value + note
        }
    }


    fun getNoteById(noteId : String) {
        _note.value = _notes.value.find {it.id == noteId}
    }
}