package fr.eseo.ld.android.cp.notes.model

import java.util.Date

data class Note(
    val id: String = "",
    val author: String = "",
    val creationDate: Date = Date(),
    val modificationDate: Date = Date(),
    var title: String = "",
    var body: String = ""
) {}
