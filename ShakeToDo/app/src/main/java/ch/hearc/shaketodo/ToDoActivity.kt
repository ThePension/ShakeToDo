package ch.hearc.shaketodo

import android.net.Uri
import android.content.Intent
import android.net.Uri
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.shaketodo.adapter.CustomAdapter
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.ToDo
import ch.hearc.shaketodo.model.ToDoDao
import org.w3c.dom.Text
import java.util.Formatter
import java.util.concurrent.Executors

class ToDoActivity : AppCompatActivity() {


    private lateinit var todo : ToDo
    private lateinit var todoDao : ToDoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val todoId = intent.getLongExtra("todoId", -1L)
        val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
        todoDao = database.todoDao()

        if (todoId == -1L)
        {
            Log.i("MainActivity", "ERREUR - TODO NOT FOUND")
            finish()
        }
        val todoName = findViewById<TextView>(R.id.todo_name)
        val todoCreatedate = findViewById<TextView>(R.id.todo_createdate)
        val todoDuedate = findViewById<TextView>(R.id.todo_duedate)
        val todoCompleted = findViewById<CheckBox>(R.id.todo_completed)
        val todoPriority = findViewById<RatingBar>(R.id.todo_priority)
        val imageView = findViewById<ImageView>(R.id.imageView)

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

        /*val btnDelete = findViewById<Button>(R.id.delete_todo_button)
        btnDelete.setOnClickListener {
            deleteTodo()
        }*/
    }

    public fun onDeleteButtonClick(view: View) {
        deleteTodo()
    }

    public fun onUpdateButtonClick(view: View) {
        showUpdateTodo()
    }

    private fun deleteTodo() {
        Executors.newSingleThreadExecutor().execute {
            todoDao.delete(todo)
        }
        finish()
    }

    private fun showUpdateTodo() {
        // Create an Intent to start NewActivity
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra("todoId", todo.id)
        // Start the NewActivity
        startActivity(intent)
    }
}