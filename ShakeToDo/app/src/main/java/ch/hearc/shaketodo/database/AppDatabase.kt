package ch.hearc.shaketodo.database

import android.content.Context
import androidx.room.*
import ch.hearc.shaketodo.model.ToDo
import ch.hearc.shaketodo.model.ToDoDao

@Database(entities = [ToDo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo.db"
                ).build()
                INSTANCE = instance

                return instance
            }
        }
    }
}
