package BottomSheet

import Database.ApiService.CategoryService
import Database.SQLLite.CategoryDBHandler
import Models.Category
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.expensemanagementsystem.databinding.FragmentAddCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddCategoryFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddCategoryBinding
    private lateinit var categoryDBHandler : CategoryDBHandler

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        categoryDBHandler = CategoryDBHandler(requireActivity(), "EMS.db", null, 3)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        binding.saveButton.setOnClickListener{
            saveAction()
        }
        return binding.root
    }

    private fun saveAction()
    {
        if(binding.title.text.toString().isNotEmpty()){
            save()
        }else{
            Toast.makeText(requireActivity(), "Title must be informed", Toast.LENGTH_LONG).show()
        }
    }

    private fun save() {
        val collection = db.collection("Categories")
        val category = Category("", binding.title.text.toString(), binding.description.text.toString())
        // Data to be saved
        val data = hashMapOf(
            "title" to category.title,
            "description" to category.description,
        )

        // Add the data as a new document with a generated ID
        collection
            .add(data)
            .addOnSuccessListener {
                // Document added successfully
                try{
                    categoryDBHandler.insert(category)
                }catch (e: SQLiteException){
                    Toast.makeText(requireActivity(), "Error on local save", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                binding.title.setText("")
                binding.description.setText("")
                dismiss()

                Toast.makeText(requireActivity(), "Successfully Saved", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                // Failed to add document
                Toast.makeText(requireActivity(), "Error on save", Toast.LENGTH_LONG).show()
                return@addOnFailureListener
            }
    }
}