package ch.hearc.shaketodo


import android.app.Application
import android.net.Uri
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.ToDo
import ch.hearc.shaketodo.model.ToDoDao
import ch.hearc.shaketodo.service.Receiver
import java.security.AccessController.getContext
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.sqrt


class ToDoActivity : AppCompatActivity() {


    private lateinit var todo : ToDo
    private lateinit var todoDao : ToDoDao
    private lateinit var todoName : TextView
    private lateinit var todoCreatedate:TextView
    private lateinit var todoDuedate:TextView
    private lateinit var todoCompleted:CheckBox
    private  lateinit var todoPriority:RatingBar
    private lateinit var imageView:ImageView
    private var todoId: Long = -1L

    // Used for shake detection
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private val launchCalledActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            updateValues()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        // Getting the Sensor Manager instance
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Objects.requireNonNull(sensorManager)!!
            .registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

        todoId = intent.getLongExtra("todoId", -1L)
        val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
        todoDao = database.todoDao()

        if (todoId == -1L)
        {
            Log.i("ToDoActivity", "ERREUR - TODO NOT FOUND")
            finish()
        }
        todoName = findViewById(R.id.todo_name)
        todoCreatedate = findViewById(R.id.todo_createdate)
        todoDuedate = findViewById(R.id.todo_duedate)
        todoCompleted = findViewById(R.id.todo_completed)
        todoPriority = findViewById(R.id.todo_priority)
        imageView = findViewById(R.id.imageView)

        Executors.newSingleThreadExecutor().execute {
            todo = todoDao.findById(todoId)
            todoName.text = todo.name
            todoCreatedate.text = todo.createdate
            todoDuedate.text = todo.duedate
            todoCompleted.isChecked = todo.completed ?: false
            val rating = todo.priority ?: 0
            todoPriority.rating = rating / 2F
            imageView?.setImageURI(Uri.parse(todo.imagelocation))
        }
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // Fetching x,y,z values
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            // Getting current accelerations
            // with the help of fetched x,y,z values
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            // Set the ToDo state as Done if acceleration value is over 12
            if (acceleration > 10) {
                if (!todo.completed!!)
                {
                    Toast.makeText(applicationContext, "ToDo marked as completed", Toast.LENGTH_SHORT).show()
                    todo.completed = true
                    updateTodo()

                    // Update UI
                    findViewById<CheckBox>(R.id.todo_completed).isChecked = true
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }



    fun onDeleteButtonClick(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Todo")
        builder.setMessage("Are you sure you want to delete this todo?")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteTodo()
        }
        builder.setNegativeButton("No", null)
        val dialog = builder.create()
        dialog.show()
    }

    fun onUpdateButtonClick(view: View) {
        showUpdateTodo()
    }

    private fun deleteTodo() {
        Executors.newSingleThreadExecutor().execute {
            todoDao.delete(todo)
        }
        finish()
    }

    private fun updateTodo()
    {
        Executors.newSingleThreadExecutor().execute {
            todoDao.update(todo)
        }
    }

    private fun showUpdateTodo() {
        // Create an Intent to start NewActivity
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra("todoId", todo.id)
        // Start the NewActivity
        launchCalledActivity.launch(intent)
    }

    private fun updateValues() {
        Executors.newSingleThreadExecutor().execute {
            todo = todoDao.findById(todoId)
            todoName.text = todo.name
            todoCreatedate.text = todo.createdate
            todoDuedate.text = todo.duedate
            todoCompleted.isChecked = todo.completed ?: false
            val rating = todo.priority ?: 0
            todoPriority.rating = rating / 2F
            imageView?.setImageURI(Uri.parse(todo.imagelocation))
        }
    }
}