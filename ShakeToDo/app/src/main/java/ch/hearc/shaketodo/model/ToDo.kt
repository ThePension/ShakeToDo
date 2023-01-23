package ch.hearc.shaketodo.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "todos")
data class ToDo (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name") val name: String?, // ? pour indiquer nullable
    @ColumnInfo(name = "createdate") val createdate: String?,
    @ColumnInfo(name = "duedate") val duedate: String?,
    @ColumnInfo(name = "notes") val notes: String?,
    @ColumnInfo(name = "imagelocation") val imagelocation: String?,
    @ColumnInfo(name = "priority") val priority: Int?,
    @ColumnInfo(name = "completed") val completed: Boolean?
) {

}