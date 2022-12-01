package ch.hearc.shaketodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.room.Room
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.ToDo
import java.util.concurrent.Executors
import java.util.stream.IntStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
        val todoDao = database.todoDao()

        Executors.newSingleThreadExecutor().execute {
            todoDao?.insertAll(ToDo(
                0,
                "Todo 1",
                "0000001",
                "0000002",
                "These are the notes",
                "/Path/To/image",
                5,
                false),
                ToDo(
                0,
                "Todo 2",
                "0000001",
                "0000002",
                "These are the notes",
                "/Path/To/image",
                5,
                false))
        }

        todoDao?.getAll()?.observe(this, Observer { todos ->
            Log.i("MainActivity", "todos=$todos")
        })
    }
}