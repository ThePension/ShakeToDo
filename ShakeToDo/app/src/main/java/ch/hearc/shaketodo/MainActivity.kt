package ch.hearc.shaketodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.room.Room
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.FactoryToDo
import ch.hearc.shaketodo.model.ToDo
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Executors
import java.util.stream.IntStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
        val todoDao = database.todoDao()

        Executors.newSingleThreadExecutor().execute {
            todoDao?.insertAll(
                FactoryToDo.createToDo(
                    "Nettoyer la caisse",
                    "24/11/2022",
                    "Il faut absolument nettoyer la voiture avant noël sinon c'est la merde",
                    "./images/image.png",
                    6,
                    false
                ),
                FactoryToDo.createToDo(
                    "Nettoyer Tim",
                    "24/12/2022",
                    "Il faut absolument nettoyer Tim avant noël sinon c'est la merde",
                    "./images/image.png",
                    100,
                    false
                )
            )
        }

        todoDao?.getAll()?.observe(this, Observer { todos ->
            Log.i("MainActivity", "todos=$todos")
        })
    }
}