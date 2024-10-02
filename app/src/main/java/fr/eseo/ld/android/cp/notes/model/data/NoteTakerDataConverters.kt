package fr.eseo.ld.android.cp.notes.model.data

import androidx.room.TypeConverter
import java.util.Date

class NoteTakerDataConverters {

    @TypeConverter
    fun fromTimestamp(value : Long?) : Date? {
        return value?.let{ Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(value : Date?) : Long? {
        return value?.time
    }


}