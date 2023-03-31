package ch.hearc.shaketodo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.hearc.shaketodo.AddActivity
import ch.hearc.shaketodo.R
import ch.hearc.shaketodo.ToDoActivity
import ch.hearc.shaketodo.adapter.CustomAdapter
import ch.hearc.shaketodo.database.AppDatabase
import ch.hearc.shaketodo.databinding.FragmentHomeBinding
import ch.hearc.shaketodo.model.ToDo

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var listView: ListView

    // Declare ArrayList to store list items
    private val listItems = arrayListOf<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database: AppDatabase by lazy { AppDatabase.getInstance(requireActivity().application) }

        val todoDao = database.todoDao()

        listView = binding.listView

        todoDao.getAll().observe(viewLifecycleOwner, Observer { todos ->
            val arrayAdapter = CustomAdapter(requireContext(), todos)

            listView.adapter = arrayAdapter
        })

        // Set the list of todos using the view model
        /*homeViewModel.todos.observe(viewLifecycleOwner, Observer {
            // Set the list of todos using the view model
            val adapter = CustomAdapter(this, it)
            listView.adapter = adapter
        })*/

        // Find the add button container
        val addButtonContainer = root.findViewById<LinearLayout>(R.id.add_button_container)

        // Add listener on listView
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Log.i("MainActivity", "Clicked on item")
            showTodoItem(parent.getItemAtPosition(position) as ToDo)
        }

        // Set the on click listener on the container
        addButtonContainer.setOnClickListener {
            // Your code here, for example:
            showAddItem()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTodoItem(todo: ToDo) {
        // Start ToDoActivity and pass the id of the todo
        val intent = Intent(requireContext(), ToDoActivity::class.java)
        intent.putExtra("todoId", todo.id)
        startActivity(intent)
    }

    private fun showAddItem() {
        // Create an Intent to start NewActivity
        val intent = Intent(requireContext(), AddActivity::class.java)
        // Start the NewActivity
        startActivity(intent)
    }
}