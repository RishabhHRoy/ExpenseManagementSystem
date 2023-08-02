package Fragments

import Adapters.BudgetAdapter
import Adapters.CategoryAdapter
import BottomSheet.AddBudgetFragment
import BottomSheet.AddCategoryFragment
import BottomSheet.EditBudgetFragment
import BottomSheet.EditCategoryFragment
import Database.ApiService.BudgetService
import Database.ApiService.CategoryService
import Database.SQLLite.BudgetDBHandler
import Database.SQLLite.CategoryDBHandler
import Models.Budget
import Models.Category
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
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentBudgetBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BudgetFragment : Fragment() {
    private lateinit var binding : FragmentBudgetBinding
    private lateinit var budgetDBHandler : BudgetDBHandler
    private lateinit var categoryDBHandler: CategoryDBHandler

    private val itemList = ArrayList<Budget>()
    private val categoryList = ArrayList<Category>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BudgetAdapter

    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("Budgets")
    private val categoriesRef = db.collection("Categories")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        budgetDBHandler = BudgetDBHandler(requireActivity(), "EMS.db", null, 1)
        categoryDBHandler = CategoryDBHandler(requireActivity(), "EMS.db", null, 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initiateFab()

        binding = FragmentBudgetBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }

        getCategories()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        budgetDBHandler.close()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateRecyclerView()

        GetAll()
    }

    private fun initiateFab(){
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)

        fab.isVisible = true

        fab.setOnClickListener(null)

        fab.setOnClickListener{
            AddBudgetFragment().show(requireActivity().supportFragmentManager, "addBudgetTag")
        }
    }

    private fun initiateRecyclerView(){
        recyclerView = binding.recyclerView
        adapter = BudgetAdapter(itemList)

        adapter.clickEvents =
            object : BudgetAdapter.ClickEvents{
                override fun onItemClickEvent(item: Budget) {
                    editItems(item)
                }

                override fun onItemLongClickEvent(item: Budget) {
                    Delete(item.id)
                }
            }

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter

        val divideDecorator = DividerItemDecoration(recyclerView.context, 1)
        recyclerView.addItemDecoration(divideDecorator)
    }

    private fun editItems(item: Budget){
        var bundle = Bundle()

        bundle.putString("id", item.id)
        bundle.putString("title", item.title)
        bundle.putString("description", item.description)
        bundle.putString("value", item.value.toString())
        bundle.putString("categoryId", item.categoryId)

        val frg = EditBudgetFragment()
        frg.arguments = bundle
        frg.show(requireActivity().supportFragmentManager, "editBudgetTag")

        adapter.notifyDataSetChanged()
        true
    }

    private fun GetAll(){
        initiateRecyclerView()

        collectionRef
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()

                for (document in result) {
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val description = document.getString("description") ?: ""
                    val value = document.getDouble("value") ?: 0.0
                    val categoryId = document.getString("categoryId") ?: ""
                    val budget = Budget(id, title, description, value, getCategoryName(categoryId))

                    itemList.add(budget)
                }

                adapter.notifyDataSetChanged()
                return@addOnSuccessListener
            }
            .addOnFailureListener { exception ->
                var items = budgetDBHandler.readAll()

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
                budgetDBHandler.delete(id)
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