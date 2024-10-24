package com.dicoding.restaurantreview.ui.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.restaurantreview.R
import com.dicoding.restaurantreview.data.EventRepository
import com.dicoding.restaurantreview.data.local.entity.FavoriteEventEntity
import com.dicoding.restaurantreview.data.local.room.FavoriteEventDatabase
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import com.dicoding.restaurantreview.databinding.ActivityDetailEventBinding
import com.dicoding.restaurantreview.ui.DetailViewModel



class DetailEventActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailEventBinding

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        Log.d("DetailEventActivity", "Received event ID: $eventId")

        if (eventId == -1) {
            Log.e("DetailEventActivity", "Invalid event ID")
            Toast.makeText(this, "Invalid event ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val apiService = ApiConfig.getApiService()
        val database = FavoriteEventDatabase.getDatabase(this)
        val eventRepository = EventRepository(database.favoriteEventDao(), apiService)
        val factory = DetailViewModelFactory(eventRepository)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        detailViewModel.fetchEventDetail(eventId)

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.eventDetail.observe(this) { event ->
            event?.let { eventDetail ->
                Glide.with(this).load(eventDetail.mediaCover).into(binding.imageView2)
                binding.tvTitleEvent.text = eventDetail.name
                binding.tvOwnerName.text = eventDetail.ownerName
                binding.tvDescription.text = HtmlCompat.fromHtml(
                    eventDetail.description,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                binding.tvBeginTime.text = eventDetail.beginTime
                binding.tvQuota.text = getString(R.string.quota_text, eventDetail.quota - eventDetail.registrants)
                binding.btnToLink.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventDetail.link))
                    startActivity(intent)
                }

                // Set FAB click listener
                binding.fabBookmark.setOnClickListener {
                    detailViewModel.toggleFavoriteEvent(eventDetail)
                }

                // Observe favorite status
                detailViewModel.getEventById(eventDetail.id.toString()).observe(this) { favoriteEvent ->
                    if (favoriteEvent != null) {
                        binding.fabBookmark.setImageResource(R.drawable.baseline_bookmark_added_24)
                    } else {
                        binding.fabBookmark.setImageResource(R.drawable.baseline_bookmark_border_24)
                    }
                }
            } ?: run {
                Log.e("DetailEventActivity", "Event detail is null")
                Toast.makeText(this, "Event detail not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        detailViewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.scrollView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }



}