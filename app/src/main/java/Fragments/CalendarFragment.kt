package Fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentBudgetBinding
import com.example.expensemanagementsystem.databinding.FragmentCalendarBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CalendarFragment : Fragment() {
    private lateinit var binding : FragmentCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = false

        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openGoogleCalendar()
    }

    private fun openGoogleCalendar() {
        val calendarIntent = Intent(Intent.ACTION_VIEW)
        calendarIntent.data = Uri.parse("content://com.android.calendar/time/")

        // If you want to specify a specific date, you can use the following format:
        // calendarIntent.setData(Uri.parse("content://com.android.calendar/time/epoch?time=" + desiredTimeInMilliseconds));

        try {
            startActivity(calendarIntent);
        } catch (e: ActivityNotFoundException) {
            // Handle the case where the Google Calendar app is not installed on the device.
            // You can provide a fallback or show a message to the user to install the app.
            Toast.makeText(requireActivity(),  "Calendar not found", Toast.LENGTH_LONG).show()
        }
    }
}