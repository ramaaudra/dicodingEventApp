package com.dicoding.restaurantreview.ui.ui.favorite

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.restaurantreview.data.local.entity.FavoriteEventEntity
import com.dicoding.restaurantreview.databinding.ItemEventBinding
import com.dicoding.restaurantreview.ui.ui.detail.DetailEventActivity

class FavoriteEventAdapter : ListAdapter<FavoriteEventEntity, FavoriteEventAdapter.FavoriteEventViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        val event = getItem(position)
        Log.d("FavoriteEventAdapter", "Binding position: $position with event ID: ${event.id}")
        holder.bind(event)
    }

    class FavoriteEventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEventEntity) {
            binding.titleEvent.text = event.name
            Glide.with(binding.imageView.context)
                .load(event.mediaCover)
                .into(binding.imageView)

            // Set OnClickListener
            binding.root.setOnClickListener {
                val context = binding.root.context
                val eventId = event.id
                Log.d("FavoriteEventAdapter", "Clicked event ID: $eventId")
                val intent = Intent(context, DetailEventActivity::class.java).apply {
                    putExtra(DetailEventActivity.EXTRA_EVENT_ID, eventId) // Use the constant key
                }
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEventEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}