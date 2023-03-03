package ch.hearc.shaketodo

import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.databinding.ActivityUpdateBinding
import ch.hearc.shaketodo.model.FactoryToDo
import ch.hearc.shaketodo.model.ToDo
import ch.hearc.shaketodo.model.ToDoDao
import java.time.LocalDate
import java.util.*
import java.util.concurrent.Executors

class UpdateActivity : AppCompatActivity() {

    private lateinit var todo : ToDo
    private lateinit var todoDao : ToDoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Find views
        val nameEditText = findViewById<EditText>(R.id.name_edit_text)
        val dueDatePicker = findViewById<DatePicker>(R.id.date_picker)
        val notesEditText = findViewById<EditText>(R.id.notes_edit_text)
        val updateButton = findViewById<Button>(R.id.update_button)

        // Create and fill number picker
        val spinner: Spinner = findViewById(R.id.priority_spinner)
        val numbers = (1..10).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Fill fields
        val todoId = intent.getLongExtra("todoId", -1L)
        val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
        todoDao = database.todoDao()

        if (todoId == -1L)
        {
            Log.i("MainActivity", "ERREUR - TODO NOT FOUND")
            finish()
        }

        Executors.newSingleThreadExecutor().execute {
            todo = todoDao.findById(todoId)
            nameEditText.setText(todo.name)
            notesEditText.setText(todo.notes)
            var splitDate = todo.duedate.toString().split("-")
            var date = intArrayOf(splitDate[0].toInt(), splitDate[1].toInt(), splitDate[2].toInt())
            dueDatePicker.updateDate(date[0], date[1], date[2])
            val rating = todo.priority ?: 0
            spinner.setSelection(rating)
        }
    }

    fun onUpdateButtonClick() {
        Executors.newSingleThreadExecutor().execute {
            todoDao.update(todo)
        }
    }
}