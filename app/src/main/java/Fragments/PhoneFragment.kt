package Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentPhoneBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PhoneFragment : Fragment() {
    private lateinit var binding : FragmentPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = false

        binding = FragmentPhoneBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }

        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:00292701938791235123")
        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 102)
        }else{
            try{
                startActivity(intent)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}