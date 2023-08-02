package Adapters

import Models.Expense
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanagementsystem.R

class ExpenseAdapter(private val itemList: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val costTextView: TextView = itemView.findViewById(R.id.costTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
    }

    var clickEvents: ClickEvents? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleTextView.text = item.title
        holder.costTextView.text = item.cost.toString()
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
        fun onItemClickEvent(item: Expense)
        fun onItemLongClickEvent(item: Expense)
    }
}