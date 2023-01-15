package ch.hearc.shaketodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ListView
import androidx.lifecycle.Observer
import ch.hearc.shaketodo.adapter.CustomAdapter
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.model.FactoryToDo
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    // Declare ArrayList to store list items
    private val listItems = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)

        val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
        val todoDao = database.todoDao()

        todoDao.getAll().observe(this, Observer { todos ->
            //Log.i("MainActivity", "todos=$todos")

            val arrayAdapter =
                CustomAdapter(this, todos)

            listView.adapter = arrayAdapter

            arrayAdapter.notifyDataSetChanged()
        })

        // Find the add button container
        val addButtonContainer = findViewById<LinearLayout>(R.id.add_button_container)

        // Set the on click listener on the container
        addButtonContainer.setOnClickListener {
            // Your code here, for example:
            showAddItem()
        }
    }


    private fun showAddItem() {
        // Create an Intent to start NewActivity
        val intent = Intent(this, AddActivity::class.java)

        // Start the NewActivity
        startActivity(intent)
    }
}