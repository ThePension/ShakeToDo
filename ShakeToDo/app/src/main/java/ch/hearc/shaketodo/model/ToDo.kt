package ch.hearc.shaketodo.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "todos")
data class ToDo (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name") var name: String?, // ? pour indiquer nullable
    @ColumnInfo(name = "createdate") val createdate: String?,
    @ColumnInfo(name = "duedate") var duedate: String?,
    @ColumnInfo(name = "notes") var notes: String?,
    @ColumnInfo(name = "imagelocation") var imagelocation: String?,
    @ColumnInfo(name = "priority") var priority: Int?,
    @ColumnInfo(name = "completed") var completed: Boolean?
) {

}