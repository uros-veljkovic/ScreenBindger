package com.example.screenbindger.view.fragment.genre_movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentGenreMoviesBinding
import com.example.screenbindger.databinding.FragmentTrendingBinding
import com.example.screenbindger.util.adapter.recyclerview.ItemMovieRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.decorator.GridLayoutRecyclerViewDecorator
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsFragmentArgs
import com.example.screenbindger.view.fragment.trending.TrendingFragmentDirections
import com.example.screenbindger.view.fragment.trending.TrendingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenreMoviesFragment  : Fragment(),
    OnCardItemClickListener {

    private var _binding: FragmentGenreMoviesBinding? = null
    private val binding get() = _binding!!

    private val navArgs: GenreMoviesFragmentArgs by navArgs()
    private val genreId: Int by lazy { navArgs.genreId}
    private val genreName: String? by lazy { navArgs.genreName}

    val viewModel: GenreMoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = bind(inflater, container)
        configureToolbarTitle()
        fetchData()
        initRecyclerView()
        observeViewModel()
        return view
    }

    fun configureToolbarTitle(){
        genreName?.let {
            activity?.title = genreName
        }
    }

    fun fetchData(){
        viewModel.fetchData(genreId)
    }

    fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentGenreMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun initRecyclerView() {
        binding.rvGenreMovies.also {
            it.layoutManager = GridLayoutManager(requireContext(), 2)
            it.addItemDecoration(GridLayoutRecyclerViewDecorator(2, 16, true))
            it.adapter = ItemMovieRecyclerViewAdapter(this)
        }
    }

    fun observeViewModel() {
        viewModel.response.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccessful) {
                val list = response.body()?.list?.toMutableList() ?: mutableListOf()
                binding.rvGenreMovies.adapter =
                    ItemMovieRecyclerViewAdapter(this, list)
                binding.rvGenreMovies.startLayoutAnimation()
            }
        })
    }

    override fun onCardItemClick(position: Int) {
        val movieId = viewModel.list?.get(position)?.id

        if(movieId != null){
            val action = GenreMoviesFragmentDirections.actionGenreMoviesFragmentToMovieDetailsFragment(movieId)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        activity?.setTitle(R.string.app_name)
        _binding = null
    }


}