package Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.expensemanagementsystem.R

class RssAdapter(
    private var items: List<RssItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<RssAdapter.RSSViewHolder>() {

    fun updateData(newItems: List<RssItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RSSViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rss_layout, parent, false)
        return RSSViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RSSViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener { onItemClick(currentItem.link) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class RSSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val pubDateTextView: TextView = itemView.findViewById(R.id.pubDateTextView)


        fun bind(item: RssItem) {
            titleTextView.text = item.title
            pubDateTextView.text = item.pubDate


        }
    }
}
