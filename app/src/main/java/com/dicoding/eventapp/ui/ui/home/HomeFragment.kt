package com.dicoding.eventapp.ui.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.eventapp.databinding.FragmentHomeBinding
import com.dicoding.eventapp.data.Result


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var horizontalAdapter: HorizontalAdapter
    private lateinit var verticalAdapter: VerticalAdapter
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerViewHorizontal()
        setupRecyclerViewVertical()

        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBarUpcoming.visibility = View.VISIBLE
                    binding.rvEventsHorizontal.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBarUpcoming.visibility = View.GONE
                    binding.rvEventsHorizontal.visibility = View.VISIBLE
                    horizontalAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBarUpcoming.visibility = View.GONE
                    binding.rvEventsHorizontal.visibility = View.GONE
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBarFinished.visibility = View.VISIBLE
                    binding.rvEventsVertical.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBarFinished.visibility = View.GONE
                    binding.rvEventsVertical.visibility = View.VISIBLE
                    verticalAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBarFinished.visibility = View.GONE
                    binding.rvEventsVertical.visibility = View.GONE
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
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
        binding.rvEventsHorizontal.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEventsHorizontal.setHasFixedSize(true)
        binding.rvEventsHorizontal.adapter = horizontalAdapter
    }

    private fun setupRecyclerViewVertical() {
        verticalAdapter = VerticalAdapter()
        binding.rvEventsVertical.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventsVertical.setHasFixedSize(true)
        binding.rvEventsVertical.adapter = verticalAdapter
    }
}