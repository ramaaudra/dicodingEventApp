package com.dicoding.restaurantreview.ui.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var horizontalAdapter: HorizontalAdapter
    private lateinit var verticalAdapter: VerticalAdapter
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerViewHorizontal()
        setupRecyclerViewVertical()

        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            horizontalAdapter.submitList(events)
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            verticalAdapter.submitList(events)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Log.d("HomeFragment", "Displaying Toast with message: $it")
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                homeViewModel.clearErrorMessage()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerViewHorizontal() {
        horizontalAdapter = HorizontalAdapter()
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEventsHorizontal.layoutManager = layoutManager
        binding.rvEventsHorizontal.setHasFixedSize(true)
        binding.rvEventsHorizontal.adapter = horizontalAdapter
    }

    private fun setupRecyclerViewVertical() {
        verticalAdapter = VerticalAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventsVertical.layoutManager = layoutManager
        binding.rvEventsVertical.setHasFixedSize(true)
        binding.rvEventsVertical.adapter = verticalAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvEventsVertical.visibility = View.GONE
            binding.rvEventsHorizontal.visibility = View.GONE
            binding.tvTitleFinished.visibility = View.GONE
            binding.tvTitleUpcoming.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvEventsVertical.visibility = View.VISIBLE
            binding.rvEventsHorizontal.visibility = View.VISIBLE
            binding.tvTitleFinished.visibility = View.VISIBLE
            binding.tvTitleUpcoming.visibility = View.VISIBLE
        }
    }
}