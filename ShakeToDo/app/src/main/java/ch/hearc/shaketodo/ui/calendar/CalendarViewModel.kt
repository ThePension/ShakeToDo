package ch.hearc.shaketodo.ui.calendar

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarViewModel : ViewModel() {
    fun setShakeSensibility(progress: Int) {
        _shake_sensibility.value = progress
    }

    private val _text = MutableLiveData<String>().apply {
        value = "\"Shake to complete\" sensitivity : "
    }
    val text: LiveData<String> = _text


    private val _shake_sensibility = MutableLiveData<Int>().apply {
        value = 5
    }
    val shake_sensibility: LiveData<Int> = _shake_sensibility
}