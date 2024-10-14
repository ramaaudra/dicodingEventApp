package com.dicoding.restaurantreview.ui.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.restaurantreview.databinding.ActivityDetailEventBinding
import com.dicoding.restaurantreview.ui.DetailViewModel

class DetailEventActivity : AppCompatActivity() {

    private val detailViewModel: DetailViewModel by viewModels()
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

        if (eventId != -1) {
            detailViewModel.fetchEventDetail(eventId)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.eventDetail.observe(this, { event ->
            event?.let {
                Glide.with(this).load(it.mediaCover).into(binding.imageView2)
                binding.tvTitleEvent.text = it.name
                binding.tvOwnerName.text = it.ownerName
                binding.tvDescription.text = HtmlCompat.fromHtml(
                    event.description.toString(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                binding.tvBeginTime.text = it.beginTime
                binding.tvQuota.text = it.quota.toString()
                binding.btnToLink.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                    startActivity(intent)
                }
            }
        })
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.scrollView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

}