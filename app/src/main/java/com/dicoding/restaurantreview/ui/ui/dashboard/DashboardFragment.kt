package com.dicoding.restaurantreview.ui.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.databinding.FragmentDashboardBinding
import com.dicoding.restaurantreview.ui.EventAdapter


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val dashboardViewModel by viewModels<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupSearchView()

        dashboardViewModel.event.observe(viewLifecycleOwner) { event ->
            eventAdapter.submitList(event)
        }

        dashboardViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        dashboardViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
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
        binding.rvEvents.layoutManager = layoutManager
        binding.rvEvents.setHasFixedSize(true)
        binding.rvEvents.adapter = eventAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvEvents.visibility = if (isLoading) View.GONE else View.VISIBLE
    }


    private fun setupSearchView() {
        binding.searchViewDashbooard.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    dashboardViewModel.searchUpcomingEvents(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}