package ch.hearc.shaketodo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.FactoryToDo
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddActivity : AppCompatActivity() {
    // private lateinit var viewBinding: ActivityMainBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)


        // Find views
        val nameEditText = findViewById<EditText>(R.id.name_edit_text)
        val dueDatePicker = findViewById<DatePicker>(R.id.date_picker)
        val notesEditText = findViewById<EditText>(R.id.notes_edit_text)
        val prioritySpinner = findViewById<Spinner>(R.id.priority_spinner)
        val addButton = findViewById<Button>(R.id.add_button)
        val takePictureButton = findViewById<Button>(R.id.pic_button)

        // Create and fill number picker
        val spinner: Spinner = findViewById(R.id.priority_spinner)
        val numbers = (1..10).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        takePictureButton.setOnClickListener { launchTakePictureActivity() }

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

    private fun launchTakePictureActivity() {
        // Create an Intent to start NewActivity
        val intent = Intent(this, TakePictureActivity::class.java)
        // Start the NewActivity
        startActivity(intent)
    }
}
