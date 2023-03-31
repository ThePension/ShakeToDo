package ch.hearc.shaketodo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hearc.shaketodo.model.ToDo

class HomeViewModel : ViewModel() {

    private val _todos = MutableLiveData<List<ToDo>>().apply {
        value = listOf()        
    }

    val todos: LiveData<List<ToDo>> = _todos
}