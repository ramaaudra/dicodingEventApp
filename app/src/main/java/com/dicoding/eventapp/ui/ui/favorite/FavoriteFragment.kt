package com.dicoding.eventapp.ui.ui.favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.eventapp.data.Result
import com.dicoding.eventapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteEventAdapter: FavoriteEventAdapter
    private val bookmarkedEventsViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupSearchView()

        bookmarkedEventsViewModel.bookmarkedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                    Log.d("FavoriteFragment", "Loading favorite events")
                }
                is Result.Success -> {
                    showLoading(false)
                    val data = result.data
                    //agar selalu menampilkan data terbaru
                    favoriteEventAdapter.submitList(result.data ?: emptyList())
                    Log.d("FavoriteFragment", "data fav events: $data")
                    if (data.isNullOrEmpty()) {
                        Toast.makeText(context, "No favorite events found", Toast.LENGTH_LONG).show()
                    } else {
                        Log.d("FavoriteFragment", "Favorite events loaded: $data")
                        favoriteEventAdapter.submitList(data)
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    Log.e("FavoriteFragment", "Error loading favorite events: ${result.message}")
                    Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        bookmarkedEventsViewModel.searchResults.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                    Log.d("FavoriteFragment", "Searching favorite events")
                }
                is Result.Success -> {
                    showLoading(false)
                    val data = result.data
                    if (data.isNullOrEmpty()) {
                        Toast.makeText(context, "No search results found", Toast.LENGTH_LONG).show()
                    } else {
                        Log.d("FavoriteFragment", "Search results loaded: $data")
                        favoriteEventAdapter.submitList(data)
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    Log.e("FavoriteFragment", "Error searching favorite events: ${result.message}")
                    Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        favoriteEventAdapter = FavoriteEventAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventsFavorite.layoutManager = layoutManager
        binding.rvEventsFavorite.setHasFixedSize(true)
        binding.rvEventsFavorite.adapter = favoriteEventAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvEventsFavorite.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun setupSearchView() {
        binding.searchViewFavorite.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    bookmarkedEventsViewModel.searchFavoriteEvents(it)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}