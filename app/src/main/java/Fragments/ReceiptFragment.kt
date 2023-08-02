package Fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentAddCategoryBinding
import com.example.expensemanagementsystem.databinding.FragmentReceiptBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReceiptFragment : Fragment() {
    private lateinit var binding: FragmentReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.isVisible = false

        binding = FragmentReceiptBinding.inflate(inflater, container, false)
        binding.galleryBtn.setOnClickListener() {
            pickImageGallery()
        }
        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            binding.imageView.setImageURI(data?.data)
        }
    }
}