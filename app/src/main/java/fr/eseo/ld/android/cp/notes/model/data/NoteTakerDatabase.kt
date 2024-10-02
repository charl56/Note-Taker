package fr.eseo.ld.android.cp.notes.model.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.eseo.ld.android.cp.notes.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
@TypeConverters(NoteTakerDataConverters::class)
abstract class NoteTakerDatabase : RoomDatabase() {

    abstract fun noteDao() : NoteTakerDao



}