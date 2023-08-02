package Adapters

import Models.Budget
import Models.Category
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagementsystem.R

class BudgetAdapter(private val itemList: List<Budget>) : RecyclerView.Adapter<BudgetAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val valueTextView: TextView = itemView.findViewById(R.id.valueTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
    }

    var clickEvents: BudgetAdapter.ClickEvents? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.budget_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description
        holder.valueTextView.text = item.value.toString()
        holder.categoryTextView.text = item.categoryId

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
        fun onItemClickEvent(item: Budget)
        fun onItemLongClickEvent(item: Budget)
    }
}
