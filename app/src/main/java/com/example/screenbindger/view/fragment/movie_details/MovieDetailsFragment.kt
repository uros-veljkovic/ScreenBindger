package com.example.screenbindger.view.fragment.movie_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.screenbindger.databinding.FragmentMovieDetailsBinding
import com.example.screenbindger.util.adapter.recyclerview.MovieDetailsRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_details.view.*

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    val viewModel: MovieDetailsViewModel by viewModels()
    private var _binding: FragmentMovieDetailsBinding? = null
    val binding get() = _binding!!

    val navArgs: MovieDetailsFragmentArgs by navArgs()
    val movieId: Int by lazy { navArgs.movieId }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        initRecyclerView()
        fetchData()
        observeServerResponse()
        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvMovieDetails.apply {
            adapter = MovieDetailsRecyclerViewAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun fetchData() {
        viewModel.fetchData(movieId)
    }

    private fun observeServerResponse() {
        viewModel.responseMovieDetails.observe(viewLifecycleOwner, Observer { response ->
            if (response?.body() != null && response.isSuccessful) {
                val movieEntity = response.body()
                if (movieEntity != null) {
                    (binding.rvMovieDetails.adapter as MovieDetailsRecyclerViewAdapter).addItems(
                        listOf(
                            movieEntity
                        )
                    )
                }
                binding.invalidateAll()
            }
        })

        viewModel.responseMovieDetailsCast.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccessful) {
                val items = response.body()?.casts
                (binding.rvMovieDetails.adapter as MovieDetailsRecyclerViewAdapter).addItems(items!!)
            }
            binding.invalidateAll()
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}