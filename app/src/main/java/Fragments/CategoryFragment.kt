package Fragments

import Adapters.CategoryAdapter
import BottomSheet.AddCategoryFragment
import BottomSheet.EditCategoryFragment
import Database.Firestore.FirestoreHandler
import Database.Firestore.FirestoreManager
import Database.SQLLite.CategoryDBHandler
import Models.Category
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.databinding.FragmentCategoryBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryDBHandler: CategoryDBHandler

    private val itemList = ArrayList<Category>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter

    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("Categories")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryDBHandler = CategoryDBHandler(requireActivity(), "EMS.db", null, 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initiateFab()

        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GetAll()
    }

    override fun onDestroy() {
        categoryDBHandler.close()
        super.onDestroy()
    }

    private fun initiateFab() {
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)

        fab.isVisible = true

        fab.setOnClickListener(null)

        fab.setOnClickListener {
            AddCategoryFragment().show(requireActivity().supportFragmentManager, "addCategoryTag")
        }
    }

    private fun initiateRecyclerView() {
        recyclerView = binding.recyclerView
        adapter = CategoryAdapter(itemList)

        adapter.clickEvents =
            object : CategoryAdapter.ClickEvents {
                override fun onItemClickEvent(item: Category) {
                    editItems(item)
                }

                override fun onItemLongClickEvent(item: Category) {
                    Delete(item.id)
                }
            }

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter

        val divideDecorator = DividerItemDecoration(recyclerView.context, 1)
        recyclerView.addItemDecoration(divideDecorator)
    }

    private fun editItems(item: Category) {
        var bundle = Bundle()

        bundle.putString("id", item.id)
        bundle.putString("title", item.title)
        bundle.putString("description", item.description)

        val frg = EditCategoryFragment()
        frg.arguments = bundle
        frg.show(requireActivity().supportFragmentManager, "editCategoryTag")

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
                    val description = document.getString("description") ?: ""
                    val category = Category(id, title, description)

                    itemList.add(category)
                }

                adapter.notifyDataSetChanged()
                return@addOnSuccessListener
            }
            .addOnFailureListener { exception ->
                var items = categoryDBHandler.readAll()

                itemList.clear()
                itemList.addAll(items)
                return@addOnFailureListener
            }
    }

    private fun Delete(id: String) {
        val documentRef = collectionRef.document(id)
        documentRef.delete()
            .addOnSuccessListener{
                //Delete on localDB
                categoryDBHandler.delete(id)
                Toast.makeText(requireActivity(), "Delete Successfully", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            .addOnFailureListener { exception ->
                // An error occurred while deleting the document
                Toast.makeText(requireActivity(), "Failed to delete item", Toast.LENGTH_LONG).show()
                return@addOnFailureListener
            }
    }
}