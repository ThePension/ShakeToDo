package ch.hearc.shaketodo

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.FactoryToDo
import ch.hearc.shaketodo.service.Receiver
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AddActivity : AppCompatActivity() {
    // private lateinit var viewBinding: ActivityMainBinding

    private var imageCapture: ImageCapture? = null
    private var imageView: ImageView? = null
    private var imageUri: Uri? = null

    private lateinit var cameraExecutor: ExecutorService

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
        setContentView(R.layout.activity_add)

        // Find views
        val nameEditText = findViewById<EditText>(R.id.name_edit_text)
        val dueDatePicker = findViewById<DatePicker>(R.id.date_picker)
        val notesEditText = findViewById<EditText>(R.id.notes_edit_text)
        val addButton = findViewById<Button>(R.id.add_button)
        val takePictureButton = findViewById<Button>(R.id.pic_button)

        imageView = findViewById<ImageView>(R.id.imageView)

        // Create and fill number picker
        val spinner: Spinner = findViewById(R.id.priority_spinner)
        val numbers = (1..10).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        takePictureButton.setOnClickListener { launchTakePictureActivity() }

        // Set add button click listener
        addButton.setOnClickListener {
            var imageUriString: String = "none"

            if (imageUri != null) {
                imageUriString = imageUri.toString()
            }

            val todo = FactoryToDo.createToDo(
                name = nameEditText.text.toString(),
                duedate = "" + dueDatePicker.year + "-" + dueDatePicker.month + "-" + dueDatePicker.dayOfMonth,
                notes = notesEditText.text.toString(),
                imageLocation = imageUriString,
                priority = spinner.selectedItem as Int,
                completed = false
            )
            val database: AppDatabase by lazy { AppDatabase.getInstance(applicationContext as Application) }
            val todoDao = database.todoDao()

            Executors.newSingleThreadExecutor().execute {
                todoDao.insertAll(todo)
            }
            // Create the intent to notify the user
            val title = todo.name.toString()
            val content = "This ToDo is due tomorrow!"
            // Get time in milliseconds from dueDatePicker
            // Get the selected year, month, and day from the DatePicker
            val year = dueDatePicker.year
            val month = dueDatePicker.month
            val day = dueDatePicker.dayOfMonth

            // Create a Calendar instance and set its fields to the selected date
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day - 1) // Alarm will be set the previous day
                set(Calendar.HOUR_OF_DAY, 8) // Alarm will be set at 8am the previous day
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // This triggertime would set the alarm for the day before the dueDate at 8am
            val triggerDate = calendar.time
            // For the purpose of our application (it being a school project)
            // We decided to use a trigger time 10 seconds after the creation of the ToDo
            // This is so during the presentation we don't have to wait for the dueDate to
            // demonstrate notification capabilities


            // Get the current time
            val currentTime = Calendar.getInstance().time

            // Add 10 seconds
            val futureTime = Calendar.getInstance().apply {
                time = currentTime
                add(Calendar.SECOND, 10)
            }.time
            Log.i("NotifLog", "Creating notification with title=$title, content=$content, triggerDate=$futureTime")

            Receiver.createNotification(this, title, content, futureTime)
            finish()
        }
    }

    private fun launchTakePictureActivity() {
        // Create an Intent to start NewActivity
        val intent = Intent(this, TakePictureActivity::class.java)
        // Start the NewActivity
        startForResult.launch(intent)
    }
}
