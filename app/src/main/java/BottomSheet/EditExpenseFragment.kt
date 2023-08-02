package BottomSheet

import Database.SQLLite.BudgetDBHandler
import Database.SQLLite.CategoryDBHandler
import Database.SQLLite.ExpenseDBHandler
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
import com.example.expensemanagementsystem.databinding.FragmentEditExpenseBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class EditExpenseFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentEditExpenseBinding
    private lateinit var expenseDBHandler: ExpenseDBHandler
    private lateinit var categoryDBHandler: CategoryDBHandler
    private lateinit var budgetDBHandler: BudgetDBHandler

    private val db = FirebaseFirestore.getInstance()
    private val budgetRef = db.collection("Budgets")
    private val categoryRef = db.collection("Categories")
    private val expenseRef = db.collection("Expenses")

    private var categoryList = ArrayList<Category>()
    private lateinit var dropdownSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
        expenseDBHandler = ExpenseDBHandler(activity, "EMS.db", null, 2)
        categoryDBHandler = CategoryDBHandler(activity, "EMS.db", null, 2)
        budgetDBHandler = BudgetDBHandler(activity, "EMS.db", null, 2)

        populateCategoryDropdown()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditExpenseBinding.inflate(inflater, container, false)

        binding.title.setText(arguments?.getString("title"))
        binding.cost.setText(arguments?.getString("cost"))
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
            val categoryId = getCategoryID(binding.spinner.selectedItemId.toString().toInt())
            val expenseId = arguments?.getString("id")

            //On add expense check if there is enough budget
            if (hasBudget(categoryId)) {
                val success = categoryId?.let {
                    Expense(
                        "", binding.title.text.toString(), binding.cost.text.toString().toDouble(),
                        it
                    )
                }

                if (success == null) {
                    Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                } else {
                    update(expenseId ?: "")

                    Toast.makeText(requireActivity(), "Successfully registered", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(requireActivity(), "Budget has been reached", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(requireActivity(), "Title must be informed", Toast.LENGTH_LONG).show()
        }
    }

    private fun update(id: String) {
        val reference = expenseRef.document(id)

        val expense = Expense(
            arguments?.getString("id") ?: "",
            binding.title.text.toString(),
            binding.cost.text.toString().toDouble(),
            getCategoryID(binding.spinner.selectedItemId.toInt())
        )

        // Data to be saved
        val data = hashMapOf(
            "id" to expense.id,
            "title" to expense.title,
            "cost" to expense.cost,
            "categoryId" to expense.categoryId
        )

        // Add the data as a new document with a generated ID
        reference
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
                // Document added successfully
                try{
                    expenseDBHandler.update(expense.id, expense)
                }catch (e: SQLiteException){
                    Toast.makeText(requireActivity(), "Error on local update", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }
                binding.title.setText("")
                binding.cost.setText("")

                dismiss()

                Toast.makeText(requireActivity(), "Successfully Saved", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                // Failed to add document
                Toast.makeText(requireActivity(), "Error on save", Toast.LENGTH_LONG).show()
            }
    }

    private fun populateCategoryDropdown() {
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

    private fun setCategorySpinner() {
        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryList.map { "Title: " + it.title })

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        dropdownSpinner.adapter = adapter
    }

    private fun getCategoryID(i: Int): String {
        return categoryList[i].id
    }

    private fun hasBudgetLocal(categoryID: String, expenseID: String): Boolean {
        val budgetList = budgetDBHandler.getCategoryBudget(categoryID)
        val expenseTotal = expenseDBHandler.getTotalCostOnUpdate(
            categoryID,
            expenseID
        ) + binding.cost.text.toString().toDouble()

        if (budgetList.isNotEmpty()) {
            if (expenseTotal > budgetList[0].value) {
                return false
            }
        } else {
            return false
        }

        return true
    }

    private fun hasBudget(categoryID: String): Boolean {
            var hBudget = true
          budgetRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.getString("categoryId") == categoryID) {
                        val id = document.id
                        val title = document.getString("title") ?: ""
                        val description = document.getString("description") ?: ""
                        val cost = document.getDouble("cost") ?: 0.0
                        val categoryId = document.getString("categoryId") ?: ""

                        val budget = Budget(id, title, description, cost, categoryId)
                        val totalCost = getTotalExpenseInCategory(categoryID)

                        if(totalCost > budget.value){
                            hBudget = false
                            return@addOnSuccessListener
                        }
                        hBudget = false
                        return@addOnSuccessListener
                    }
                }
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                hBudget = true
                return@addOnFailureListener
            }
            return true
        }

        private fun getTotalExpenseInCategory(categoryId: String) : Double{
            var total = 0.0

            expenseRef.get()
                .addOnSuccessListener {result ->
                    for(document in result){
                        val category = document.getString("categoryId") ?: ""
                        if(category == categoryId){
                            total += document.getDouble("cost") ?: 0.0
                        }
                    }
                }
                .addOnFailureListener {

                }
            return total
        }
    }
