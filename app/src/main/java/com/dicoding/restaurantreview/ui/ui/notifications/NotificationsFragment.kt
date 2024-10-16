package com.dicoding.restaurantreview.ui.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.databinding.FragmentDashboardBinding
import com.dicoding.restaurantreview.databinding.FragmentNotificationsBinding
import com.dicoding.restaurantreview.ui.EventAdapter

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val finishedViewModel by viewModels<NotificationsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupSearchView()

        finishedViewModel.event.observe(viewLifecycleOwner) { event ->
            eventAdapter.submitList(event)
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        finishedViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            eventAdapter.submitList(searchResults)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventsNotification.layoutManager = layoutManager
        binding.rvEventsNotification.setHasFixedSize(true) // Set fixed size
        binding.rvEventsNotification.adapter = eventAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvEventsNotification.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun setupSearchView() {
        binding.searchViewNotification.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    finishedViewModel.searchFinishedEvents(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}