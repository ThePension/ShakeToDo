package ch.hearc.shaketodo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.ToDo
import ch.hearc.shaketodo.model.ToDoDao
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UpdateActivity : AppCompatActivity() {

    private lateinit var todo: ToDo
    private lateinit var todoDao: ToDoDao

    private var imageCapture: ImageCapture? = null
    private var imageView: ImageView? = null
    private var imageUri: Uri? = null
    private lateinit var nameEditText: EditText
    private lateinit var dueDatePicker: DatePicker
    private lateinit var notesEditText: EditText
    private lateinit var spinner : Spinner

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

            // Get the image URI
            imageUri = Uri.parse(intent?.getStringExtra("imageUri"))

            // do stuff here
            Log.i("Image handling", "Image URI : ${imageUri.toString()}")

            imageView?.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Find views
        nameEditText = findViewById<EditText>(R.id.name_edit_text)
        dueDatePicker = findViewById<DatePicker>(R.id.date_picker)
        notesEditText = findViewById<EditText>(R.id.notes_edit_text)
        
        imageView = findViewById<ImageView>(R.id.imageView)

        val updateImageButton = findViewById<Button>(R.id.update_image_button)
        val updateButton = findViewById<Button>(R.id.update_button)

        // Create and fill number picker

        val numbers = (1..10).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner = findViewById(R.id.priority_spinner)
        spinner.adapter = adapter

        // Fill fields
        val todoId = intent.getLongExtra("todoId", -1L)
        val database: AppDatabase by lazy { AppDatabase.getInstance(applicationContext as Application) }
        todoDao = database.todoDao()

        if (todoId == -1L) {
            Log.i("MainActivity", "ERROR - TODO NOT FOUND")
            finish()
        }

        Executors.newSingleThreadExecutor().execute {
            todo = todoDao.findById(todoId)
            nameEditText.setText(todo.name)
            notesEditText.setText(todo.notes)
            var splitDate = todo.duedate.toString().split("-")
            var date = intArrayOf(splitDate[0].toInt(), splitDate[1].toInt(), splitDate[2].toInt())
            dueDatePicker.updateDate(date[0], date[1], date[2])
            todo.priority?.let { spinner.setSelection(it - 1) }

        }
    }

    fun onUpdateImageButtonClick(view: View) {
        // Create an Intent to start NewActivity
        val intent = Intent(this, TakePictureActivity::class.java)
        // Start the NewActivity
        startForResult.launch(intent)
    }

    fun onUpdateButtonClick(view: View) {
        val name = nameEditText.text.toString()
        val notes = notesEditText.text.toString()
        val priority = spinner.selectedItem.toString().toInt()
        val dueDate = "${dueDatePicker.year}-${dueDatePicker.month}-${dueDatePicker.dayOfMonth}"

        todo.name =name;
        todo.notes = notes
        todo.priority = priority
        todo.duedate = dueDate
        if(imageUri!=null) {
            todo.imagelocation = imageUri.toString()
        }else{
            todo.imagelocation=todo.imagelocation
        }
        Executors.newSingleThreadExecutor().execute { todoDao.update(todo) }

        // Close the activity
        finish()
    }
}
