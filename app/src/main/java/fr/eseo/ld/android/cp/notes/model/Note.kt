package fr.eseo.ld.android.cp.notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: String = "",
    val author: String = "",
    val creationDate: Date = Date(),
    val modificationDate: Date = Date(),
    var title: String = "",
    var body: String = ""
) {}
