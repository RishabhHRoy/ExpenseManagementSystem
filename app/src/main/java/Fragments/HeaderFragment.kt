package Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentReceiptBinding
import com.example.expensemanagementsystem.databinding.HeaderBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception

class HeaderFragment : Fragment() {
    private lateinit var binding: HeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HeaderBinding.inflate(inflater, container, false)

        binding.profileName.setOnLongClickListener{
            pickImageGallery()
        }

        binding.profileName.setOnLongClickListener{
            changeName()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    private fun changeName() : Boolean{
        return true
    }

    private fun pickImageGallery() : Boolean{
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        try{
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }catch (e: Exception){
            return false
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ReceiptFragment.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            binding.profileImage.setImageURI(data?.data)
        }
    }
}