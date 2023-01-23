package ch.hearc.shaketodo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.FactoryToDo
import java.util.concurrent.Executors

class AddActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Find views
        val nameEditText = findViewById<EditText>(R.id.name_edit_text)
        val dueDatePicker = findViewById<DatePicker>(R.id.date_picker)
        val notesEditText = findViewById<EditText>(R.id.notes_edit_text)
        val prioritySpinner = findViewById<Spinner>(R.id.priority_spinner)
        val addButton = findViewById<Button>(R.id.add_button)

        // Create and fill number picker
        val spinner: Spinner = findViewById(R.id.priority_spinner)
        val numbers = (1..10).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        // Set add button click listener
        addButton.setOnClickListener {
            val todo = FactoryToDo.createToDo(
                name = nameEditText.text.toString(),
                duedate = dueDatePicker.toString(),
                notes = notesEditText.text.toString(),
                imageLocation = "./images/image.png", // TODO
                priority = spinner.selectedItem as Int,
                completed = false
            )
            val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
            val todoDao = database.todoDao()

            Executors.newSingleThreadExecutor().execute {
                todoDao.insertAll(todo)
            }
            finish()
        }
    }
}
