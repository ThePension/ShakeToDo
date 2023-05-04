package ch.hearc.shaketodo.ui.calendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ch.hearc.shaketodo.R
import ch.hearc.shaketodo.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        calendarViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val sensitivity_seekbar = binding.sensitivitySeekbar

        calendarViewModel.shake_sensibility.observe(viewLifecycleOwner) {
            sensitivity_seekbar.progress = it
        }

        // Set listener
        sensitivity_seekbar.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: android.widget.SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                // Log
                Log.d("CalendarFragment", "Shake sensibility: $progress")

                // Store the value in Shared Preferences
                calendarViewModel.setShakeSensibility(progress)

                val sharedPref = activity?.getSharedPreferences("shake_sensibility", Context.MODE_PRIVATE)

                with(sharedPref?.edit()) {
                    this?.putInt("shake_sensibility", progress)
                    this?.apply()
                }
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {
                // Nothing
            }

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                // Nothing
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}