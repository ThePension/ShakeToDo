package ch.hearc.shaketodo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.FactoryToDo
import ch.hearc.shaketodo.model.ToDo
import java.util.concurrent.Executors

class AddActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)


        // Find views
        val nameEditText = findViewById<EditText>(R.id.name_edit_text)
        //val createDateEditText = findViewById<EditText>(R.id.create_date_edit_text)
        //val dueDateEditText = findViewById<EditText>(R.id.due_date_edit_text)
        val notesEditText = findViewById<EditText>(R.id.notes_edit_text)
        //val imageLocationEditText = findViewById<EditText>(R.id.image_location_edit_text)
        //val priorityEditText = findViewById<EditText>(R.id.priority_edit_text)
        val addButton = findViewById<Button>(R.id.add_button)

        // Set add button click listener
        addButton.setOnClickListener {
            val todo = FactoryToDo.createToDo(
                name = nameEditText.text.toString(),
                duedate = "24/11/2022",
                notes = notesEditText.text.toString(),
                imageLocation = "./images/image.png",
                priority = 6,
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
