package Adapters

import BottomSheet.EditCategoryFragment
import Database.ApiService.CategoryService
import Database.SQLLite.CategoryDBHandler
import Models.Budget
import Models.Category
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagementsystem.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryAdapter(private val itemList: List<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }

    var clickEvents: ClickEvents? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description

        holder.itemView.setOnClickListener{
            clickEvents?.onItemClickEvent(item)
        }

        holder.itemView.setOnLongClickListener(){
            clickEvents?.onItemLongClickEvent(item)
            true
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface ClickEvents{
        fun onItemClickEvent(item: Category)
        fun onItemLongClickEvent(item: Category)
    }
}