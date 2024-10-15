package com.dicoding.restaurantreview.ui.ui.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.restaurantreview.data.response.ListEventsItem
import com.dicoding.restaurantreview.databinding.ItemVerticalListBinding
import com.dicoding.restaurantreview.ui.ui.DetailEventActivity

class VerticalAdapter : ListAdapter<ListEventsItem, VerticalAdapter.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemVerticalListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        Log.d("EventAdapter", "Binding position: $position with event ID: ${event.id}")
        holder.bind(event)
    }

    override fun submitList(list: List<ListEventsItem>?) {
        // Limit the list to 5 items
        val limitedList = list?.take(5)
        super.submitList(limitedList)
    }

    class EventViewHolder(private val binding: ItemVerticalListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.titleEventVertical.text = event.name
            Glide.with(binding.imageViewVertical.context)
                .load(event.mediaCover)
                .into(binding.imageViewVertical)

            // Set OnClickListener
            binding.root.setOnClickListener {
                val context = binding.root.context
                val eventId = event.id
                Log.d("EventAdapter", "Clicked event ID: $eventId")
                val intent = Intent(context, DetailEventActivity::class.java).apply {
                    putExtra(DetailEventActivity.EXTRA_EVENT_ID, eventId)
                }
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}