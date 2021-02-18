package com.example.screenbindger.view.fragment.favorite_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentFavoriteMoviesBinding
import com.example.screenbindger.util.adapter.recyclerview.BigItemMovieRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import javax.inject.Inject


class FavoriteMoviesFragment : DaggerFragment(), OnFavoriteItemClickListener {

    @Inject
    lateinit var viewModel: FavoriteMoviesViewModel

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        initRecyclerView()
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

    private fun observeViewEvents() {
        viewModel.apply {
            viewEvent.observe(viewLifecycleOwner, Observer {
                it.getContentIfNotHandled()?.let { event ->
                    when (event) {
                        is FavoriteMoviesViewEvent.MoviesLoaded -> {
                            viewState.postValue(FavoriteMoviesViewState.MoviesLoaded(event.list))
                        }
                        is FavoriteMoviesViewEvent.EmptyList -> {
                            viewState.postValue(FavoriteMoviesViewState.EmptyList)
                        }
                        is FavoriteMoviesViewEvent.Error -> {
                            viewState.postValue(FavoriteMoviesViewState.EmptyList)
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
                    is FavoriteMoviesViewState.MoviesLoaded -> {
                        binding.rvFavoriteMovies.apply {
                            adapter = BigItemMovieRecyclerViewAdapter(
                                listener = WeakReference(this@FavoriteMoviesFragment),
                                list = state.list
                            )
                        }
                    }
                    is FavoriteMoviesViewState.EmptyList -> {
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