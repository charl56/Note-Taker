package fr.eseo.ld.android.cp.notes.model

import java.util.Date

data class Note(
    val id : String,
    val author : String,
    val creationDate : Date,
    val modificationDate : Date,

    var title : String,
    var body : String
) {}
