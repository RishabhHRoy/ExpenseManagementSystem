package BottomSheet

import Database.SQLLite.BudgetDBHandler
import Database.SQLLite.CategoryDBHandler
import Models.Budget
import Models.Category
import Models.Expense
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentAddBudgetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class AddBudgetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBudgetBinding
    private lateinit var budgetDBHandler : BudgetDBHandler
    private lateinit var categoryDBHandler : CategoryDBHandler

    private val db = FirebaseFirestore.getInstance()
    private val budgetRef = db.collection("Budgets")
    private val categoryRef = db.collection("Categories")

    private var categoryList = ArrayList<Category>()
    private lateinit var dropdownSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        budgetDBHandler = BudgetDBHandler(requireActivity(), "EMS.db", null, 3)
        categoryDBHandler = CategoryDBHandler(requireActivity(), "EMS.db", null, 3)

        populateCategoryDropdown()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBudgetBinding.inflate(inflater, container, false)
        binding.saveButton.setOnClickListener{
            saveAction()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dropdownSpinner = binding.spinner
    }

    private fun saveAction()
    {
        if(binding.title.text.toString().isNotEmpty()){

            val categoryId = getCategoryID(binding.spinner.selectedItemId.toString().toInt())

            categoryExists(categoryId)
        }else{
            Toast.makeText(requireActivity(), "Title must be informed", Toast.LENGTH_LONG).show()
        }
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
                return@addOnSuccessListener
            }
            .addOnFailureListener { exception ->
                categoryList = categoryDBHandler.readAll()
                return@addOnFailureListener
            }
    }

    private fun setCategorySpinner(){
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, categoryList.map {"Title: " + it.title})

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dropdownSpinner.adapter = adapter
    }

    private fun categoryExists(id: String){
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
                            if(category == category_id){
                                //To Do category already registered in another budget
                                Toast.makeText(requireActivity(), "Budget already associated with a Category", Toast.LENGTH_LONG).show()
                                return@addOnSuccessListener
                            }
                        }

                        save()

                        Toast.makeText(requireActivity(), "Successfully registered", Toast.LENGTH_LONG).show()
                        return@addOnSuccessListener
                    }
                    .addOnFailureListener{
                        Toast.makeText(requireActivity(), "Error on fetching Budget", Toast.LENGTH_LONG).show()
                        return@addOnFailureListener
                    }
            }
            .addOnFailureListener{
                Toast.makeText(requireActivity(), "Error on fetching Category", Toast.LENGTH_LONG).show()
                return@addOnFailureListener
            }
    }

    private fun save() {
        val budget = Budget(
            "",
            binding.title.text.toString(),
            binding.description.text.toString(),
            binding.budget.text.toString().toDouble(),
            getCategoryID(binding.spinner.selectedItemId.toInt())
        )

        // Data to be saved
        val data = hashMapOf(
            "title" to budget.title,
            "description" to budget.description,
            "value" to budget.value,
            "categoryId" to budget.categoryId
        )

        // Add the data as a new document with a generated ID
        budgetRef
            .add(data)
            .addOnSuccessListener {
                // Document added successfully
                try{
                    budgetDBHandler.insert(budget)
                }catch (e: SQLiteException){
                    Toast.makeText(requireActivity(), "Error on local save", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                binding.title.setText("")
                binding.description.setText("")
                binding.budget.setText("")

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

    private fun getCategoryID(i : Int) : String{
        return categoryList[i].id
    }
}
