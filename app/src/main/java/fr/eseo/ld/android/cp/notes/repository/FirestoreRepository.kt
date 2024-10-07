package fr.eseo.ld.android.cp.notes.repository

import com.google.firebase.firestore.FirebaseFirestore
import fr.eseo.ld.android.cp.notes.model.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    private val notesCollection = firestore.collection("notes")

    fun addOrUpdateNote(note : Note) {
        notesCollection.document(note.id).set(note)
    }

    fun delete(noteId : String) {
        notesCollection.document(noteId).delete()
    }

    fun getNotes(callback : (List<Note>) -> Unit) {
        notesCollection.get().addOnSuccessListener {
                result ->
            val notes = result.map{
                it.toObject(Note::class.java)
            }
            callback(notes)
        }
    }
}