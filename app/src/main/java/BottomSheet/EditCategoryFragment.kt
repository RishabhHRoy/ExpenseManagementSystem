package BottomSheet

import Database.SQLLite.CategoryDBHandler
import Models.Category
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.expensemanagementsystem.databinding.FragmentEditCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore


class EditCategoryFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentEditCategoryBinding
    private lateinit var categoryDBHandler : CategoryDBHandler

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        categoryDBHandler = CategoryDBHandler(requireActivity(), "EMS.db", null, 2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditCategoryBinding.inflate(inflater, container, false)

        binding.title.setText(arguments?.getString("title"))
        binding.description.setText(arguments?.getString("description"))

        binding.saveButton.setOnClickListener{
            saveAction()
        }

        return binding.root
    }

    private fun saveAction()
    {
        if(binding.title.text.toString().isNotEmpty()) {
            val categoryId = arguments?.getString("id")
            var category = categoryId?.let {
                Category(
                    it,
                    binding.title.text.toString(),
                    binding.description.text.toString()
                )
            }

            if (categoryId != null) {
                if (category != null) {
                    Update(categoryId, category)
                }
            }
        }
    }

    private fun Update(id: String, category: Category){
        val documentRef = db.collection("Categories").document(id)

        val updatedData = mapOf(
            "title" to category.title,
            "description" to category.description
            // Add other fields and their updated values as needed...
        )

        documentRef
            .update(updatedData)
            .addOnSuccessListener {
                try{
                    categoryDBHandler.update(id, category)
                }catch (e: SQLiteException){
                    Toast.makeText(requireActivity(), "Error on local update", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }
                binding.title.setText("")
                binding.description.setText("")
                dismiss()

                Toast.makeText(requireActivity(), "Successfully Saved", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireActivity(), "Error on Update", Toast.LENGTH_LONG).show()
            }
    }
}