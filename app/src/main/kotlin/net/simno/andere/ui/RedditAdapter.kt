package net.simno.andere.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_listing.view.listingText
import net.simno.andere.R
import net.simno.andere.data.model.Listing
import java.util.Collections

class RedditAdapter(private val itemClick: (Int) -> Unit) :
    RecyclerView.Adapter<RedditAdapter.ViewHolder>() {

  var items: List<Listing> = Collections.emptyList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_listing, parent, false)
    return ViewHolder(view, itemClick)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(items[position])
  }

  override fun getItemCount() = items.size

  class ViewHolder(view: View, private val itemClick: (Int) -> Unit) :
      RecyclerView.ViewHolder(view) {
    fun bind(listing: Listing) {
      with(listing) {
        itemView.listingText.text = listing.data.title
        itemView.setOnClickListener { itemClick(adapterPosition) }
      }
    }
  }
}
