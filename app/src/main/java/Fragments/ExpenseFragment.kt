package Fragments

import Adapters.BudgetAdapter
import Adapters.ExpenseAdapter
import BottomSheet.AddCategoryFragment
import BottomSheet.AddExpenseFragment
import BottomSheet.EditCategoryFragment
import BottomSheet.EditExpenseFragment
import Database.ApiService.BudgetService
import Database.ApiService.CategoryService
import Database.ApiService.ExpenseService
import Database.SQLLite.CategoryDBHandler
import Database.SQLLite.ExpenseDBHandler
import Models.Budget
import Models.Category
import Models.Expense
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagementsystem.databinding.FragmentExpenseBinding
import com.example.expensemanagementsystem.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExpenseFragment : Fragment() {
    private lateinit var binding : FragmentExpenseBinding
    private lateinit var expenseDBHandler : ExpenseDBHandler
    private lateinit var categoryDBHandler: CategoryDBHandler

    private val itemList = ArrayList<Expense>()
    private val categoryList = ArrayList<Category>()

    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("Expenses")
    private val categoriesRef = db.collection("Categories")

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        expenseDBHandler = ExpenseDBHandler(requireActivity(), "EMS.db", null, 1)
        categoryDBHandler = CategoryDBHandler(requireActivity(), "EMS.db", null, 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initiateFab()

        binding = FragmentExpenseBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCategories()

        initiateRecyclerView()

        GetAll()
    }

    override fun onDestroy() {
        expenseDBHandler.close()
        super.onDestroy()
    }

    private fun initiateFab(){
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)

        fab.isVisible = true

        fab.setOnClickListener(null)

        fab.setOnClickListener{
            AddExpenseFragment().show(requireActivity().supportFragmentManager, "addExpenseTag")
        }
    }

    private fun initiateRecyclerView(){
        recyclerView = binding.recyclerView
        adapter = ExpenseAdapter(itemList)

        adapter.clickEvents =
            object : ExpenseAdapter.ClickEvents{
                override fun onItemClickEvent(item: Expense) {
                    editItems(item)
                }

                override fun onItemLongClickEvent(item: Expense) {
                    Delete(item.id)
                }
            }

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter

        val divideDecorator = DividerItemDecoration(recyclerView.context, 1)
        recyclerView.addItemDecoration(divideDecorator)
    }

    private fun editItems(item: Expense){
        var bundle = Bundle()

        bundle.putString("id", item.id)
        bundle.putString("title", item.title)
        bundle.putString("cost", item.cost.toString())
        bundle.putString("category", item.categoryId)

        val frg = EditExpenseFragment()
        frg.arguments = bundle
        frg.show(requireActivity().supportFragmentManager, "editExpenseTag")

        adapter.notifyDataSetChanged()
        true
    }

    private fun GetAll() {
        initiateRecyclerView()

        collectionRef
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (document in result) {
                    //val data = document.toObject(Category::class.java)
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val cost = document.getDouble("cost") ?: 0.0
                    val categoryId = document.getString("categoryId") ?: ""
                    val expense = Expense(id, title, cost, getCategoryName(categoryId))

                    itemList.add(expense)
                }

                adapter.notifyDataSetChanged()
                return@addOnSuccessListener
            }
            .addOnFailureListener { exception ->
                var items = expenseDBHandler.readAll()

                itemList.clear()
                itemList.addAll(items)
                return@addOnFailureListener
            }
    }

    private fun Delete(id: String){
        val documentRef = collectionRef.document(id)
        documentRef.delete()
            .addOnSuccessListener{
                //Delete on localDB
                expenseDBHandler.delete(id)
                Toast.makeText(requireActivity(), "Delete Successfully", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            .addOnFailureListener { exception ->
                // An error occurred while deleting the document
                Toast.makeText(requireActivity(), "Failed to delete item", Toast.LENGTH_LONG).show()
                return@addOnFailureListener
            }
    }

    private fun getCategories() {
        categoriesRef.get()
            .addOnSuccessListener {result ->
                categoryList.clear()
                for(document in result){
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val description = document.getString("description") ?: ""
                    val category = Category(id, title, description)
                    categoryList.add(category)
                }
                return@addOnSuccessListener
            }
            .addOnFailureListener{
                return@addOnFailureListener
            }
    }

    private fun getCategoryName(id: String) : String{
        for(item in categoryList){
            if(item.id == id) return item.title
        }
        return ""
    }
}