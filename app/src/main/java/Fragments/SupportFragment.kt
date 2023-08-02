package Fragments

import Webview.Webview
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentSupportBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SupportFragment : Fragment() {
    private lateinit var binding : FragmentSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = false

        binding = FragmentSupportBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }

        binding.sendBtn.setOnClickListener(){
            onSendClick()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun onSendClick(){
        val email = binding.emailAddress.text.toString()
        val subject = binding.subject.text.toString()
        val message = binding.message.text.toString()
        val addresses = email.split(",").toTypedArray()

        val intent = Intent(Intent.ACTION_SEND).apply{
            data = Uri.parse("mailto:")
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            if(intent.resolveActivity(requireActivity().packageManager) != null)
                startActivity(Intent.createChooser(intent, "Send email using..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(requireActivity(), "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }
}