package ch.hearc.shaketodo.database

import androidx.room.*
import ch.hearc.shaketodo.model.ToDo
import ch.hearc.shaketodo.model.ToDoDao

@Database(entities = [ToDo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDao
}
