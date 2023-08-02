package BottomSheet

import Database.SQLLite.BudgetDBHandler
import Database.SQLLite.CategoryDBHandler
import Models.Budget
import Models.Category
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentEditBudgetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class EditBudgetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentEditBudgetBinding
    private lateinit var budgetDBHandler: BudgetDBHandler
    private lateinit var categoryDBHandler: CategoryDBHandler

    private val db = FirebaseFirestore.getInstance()
    private val budgetRef = db.collection("Budgets")
    private val categoryRef = db.collection("Categories")

    private var categoryList = ArrayList<Category>()
    private lateinit var dropdownSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()

        budgetDBHandler = BudgetDBHandler(activity, "EMS.db", null, 2)
        categoryDBHandler = CategoryDBHandler(activity, "EMS.db", null, 2)

        populateCategoryDropdown()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBudgetBinding.inflate(inflater, container, false)

        binding.title.setText(arguments?.getString("title"))
        binding.description.setText(arguments?.getString("description"))
        binding.value.setText(arguments?.getString("value"))

        binding.saveButton.setOnClickListener {
            saveAction()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dropdownSpinner = binding.spinner
    }

    private fun saveAction() {
        if (binding.title.text.toString().isNotEmpty()) {
            categoryExists(getCategoryID(binding.spinner.selectedItemId.toInt()), arguments?.getString("id").toString())
        } else {
            Toast.makeText(requireActivity(), "Title must be informed", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun getCategoryID(i: Int): String {
        return categoryList[i].id
    }

    private fun populateCategoryDropdown(){
        categoryRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val description = document.getString("description") ?: ""
                    val category = Category(id, title, description)

                    categoryList.add(category)
                }

                setCategorySpinner()
            }
            .addOnFailureListener { exception ->
                categoryList = categoryDBHandler.readAll()

                Toast.makeText(requireActivity(), "Load Locally", Toast.LENGTH_LONG).show()
            }
    }

    private fun setCategorySpinner(){
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, categoryList.map {"Title: " + it.title})

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dropdownSpinner.adapter = adapter
    }

    private fun categoryExists(id: String, budgetId: String){
        val documentRef = categoryRef.document(id)
        documentRef.get()
            .addOnSuccessListener{result ->
                /*val id = result.id
                val title = result.getString("title")
                val description = result.getString("description")*/
                val category_id = result.id

                //Check if there is a budget with registered category
                budgetRef.get()
                    .addOnSuccessListener { result ->
                        for(item in result){
                            val category = item.getString("categoryId")
                            //Check if category is already registered in another budget
                            //Check if the budget is different from the current one
                            if(category == category_id && item.id != budgetId){
                                //To Do category already registered in another budget
                                return@addOnSuccessListener
                            }
                        }

                        update(budgetId)

                        Toast.makeText(requireActivity(), "Successfully registered", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(requireActivity(), "Error on fetching Budget", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener{
                Toast.makeText(requireActivity(), "Error on fetching Category", Toast.LENGTH_LONG).show()
            }
    }

    private fun update(id: String) {
        val reference = budgetRef.document(id)

        val budget = Budget(
            arguments?.getString("id") ?: "",
            binding.title.text.toString(),
            binding.description.text.toString(),
            binding.value.text.toString().toDouble(),
            getCategoryID(binding.spinner.selectedItemId.toInt())
        )

        // Data to be saved
        val data = hashMapOf(
            "id" to budget.id,
            "title" to budget.title,
            "description" to budget.description,
            "value" to budget.value,
            "categoryId" to budget.categoryId
        )

        // Add the data as a new document with a generated ID
        reference
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
                // Document added successfully
                try{
                    budgetDBHandler.update(budget.id, budget)
                }catch (e: SQLiteException){
                    Toast.makeText(requireActivity(), "Error on local update", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                binding.title.setText("")
                binding.description.setText("")
                binding.value.setText("")

                dismiss()

                Toast.makeText(requireActivity(), "Successfully Saved", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                // Failed to add document
                Toast.makeText(requireActivity(), "Error on save", Toast.LENGTH_LONG).show()
            }
    }
}
