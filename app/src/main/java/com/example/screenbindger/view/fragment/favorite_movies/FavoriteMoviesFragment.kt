package com.example.screenbindger.view.fragment.favorite_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.screenbindger.databinding.FragmentFavoriteMoviesBinding
import com.example.screenbindger.util.adapter.recyclerview.BigItemMovieRecyclerViewAdapter
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference
import javax.inject.Inject


class FavoriteMoviesFragment : DaggerFragment(), OnFavoriteItemClickListener {

    @Inject
    lateinit var viewModel: FavoriteMoviesFragmentViewModel

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        initRecyclerView()
        observeViewActions()
        observeViewEvents()
        observeViewState()

        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView() {
        val snapHelper: SnapHelper = PagerSnapHelper()
        binding.rvFavoriteMovies.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun observeViewActions() {
        viewModel.apply {
            viewAction.observe(viewLifecycleOwner, Observer {
                it.getContentIfNotHandled()?.let { action ->
                    when (action) {
                        FavoriteMoviesFragmentViewAction.FetchMovies -> {
                            viewModel.fetchFavorites()
                        }
                    }
                }
            })
        }
    }

    private fun observeViewEvents() {
        viewModel.apply {
            viewEvent.observe(viewLifecycleOwner, Observer {
                it.getContentIfNotHandled()?.let { event ->
                    when (event) {
                        is FavoriteMoviesFragmentViewEvent.MoviesLoaded -> {
                            viewState.postValue(FavoriteMoviesFragmentViewState.MoviesLoaded(event.list))
                        }
                        is FavoriteMoviesFragmentViewEvent.EmptyList -> {
                            viewState.postValue(FavoriteMoviesFragmentViewState.EmptyList)
                        }
                        is FavoriteMoviesFragmentViewEvent.Error -> {
                            viewState.postValue(FavoriteMoviesFragmentViewState.EmptyList)
                        }
                    }
                }
            })
        }
    }

    private fun observeViewState() {
        viewModel.apply {
            viewState.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    is FavoriteMoviesFragmentViewState.MoviesLoaded -> {
                        binding.rvFavoriteMovies.apply {
                            adapter = BigItemMovieRecyclerViewAdapter(
                                listener = WeakReference(this@FavoriteMoviesFragment),
                                list = state.list
                            )
                        }
                    }
                    is FavoriteMoviesFragmentViewState.EmptyList -> {
//                        setEmptyView()
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onContainerClick(movieId: Int) {
        val action =
            FavoriteMoviesFragmentDirections.actionFavoriteMoviesFragmentToMovieDetailsFragment(
                movieId
            )
        findNavController().navigate(action)
    }

    override fun onCommentsButtonClick(movieId: Int) {
        val action =
            FavoriteMoviesFragmentDirections.actionFavoriteMoviesFragmentToReviewFragment(
                movieId
            )
        findNavController().navigate(action)
    }

}