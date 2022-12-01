package ch.hearc.shaketodo

import android.util.Log
import androidx.room.Room
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.ToDo
/*
class CallDatabase {
    companion object {
        fun test(db: AppDatabase){

            val todoDao = db.todoDao()
            todoDao.insertAll(
                ToDo(
                id = 1,
                name = "nom du todo",
                completed = false,
                duedate = "date",
                createdate = "createDate",
                imagelocation="path/asd",
                notes = "ansdlfk aösdjflaö d dfla dsfl",
                priority = 69)
            )

            val todos: List<ToDo> = todoDao.getAll()

            for (todo in todos) {
                if (todo.name != null) {
                    Log.i("asdf", todo.name);
                }
            }
        }
    }
}*/