package com.example.screenbindger.view.fragment.favorite_movies

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.screenbindger.databinding.FragmentFavoriteMoviesBinding
import com.example.screenbindger.util.adapter.recyclerview.BigItemMovieRecyclerViewAdapter
import com.example.screenbindger.util.event.EventObserver
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference
import javax.inject.Inject


class FavoritesFragment : DaggerFragment(), OnFavoriteItemClickListener {

    @Inject
    lateinit var viewModel: FavoritesViewModel

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        initRecyclerView()
        initOnClickListeners()
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

    private fun initOnClickListeners() {
        binding.tabContainer.tabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == 0) {
                    viewModel.setAction(FavoritesViewAction.FetchMovies)
                } else {
                    viewModel.setAction(FavoritesViewAction.FetchTvShows)
                }
            }

        })
    }

    private fun observeViewActions() {
        viewModel.viewAction.observe(viewLifecycleOwner, EventObserver { action ->
            when (action) {
                FavoritesViewAction.FetchMovies -> {
                    viewModel.fetchFavoriteMovies()
                }
                FavoritesViewAction.FetchTvShows -> {
                    viewModel.fetchFavoriteTvShows()
                }
            }
        })
    }

    private fun observeViewEvents() {
        viewModel.viewEvent.observe(viewLifecycleOwner, EventObserver { event ->
            when (event) {
                is FavoritesViewEvent.ListLoaded -> {
                    viewModel.setState(FavoritesViewState.ListLoaded(event.list))
                }
                is FavoritesViewEvent.EmptyList,
                is FavoritesViewEvent.Error -> {
                    viewModel.setState(FavoritesViewState.EmptyList)
                }
            }
        })
    }

    private fun observeViewState() {
        viewModel.apply {
            viewState.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    is FavoritesViewState.ListLoaded -> {
                        binding.rvFavoriteMovies.apply {
                            adapter = BigItemMovieRecyclerViewAdapter(
                                listener = WeakReference(this@FavoritesFragment),
                                list = state.list
                            )
                        }
                    }
                    is FavoritesViewState.EmptyList -> {
//                        setEmptyView()
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setAction(FavoritesViewAction.FetchMovies)
        _binding = null
    }

    override fun onContainerClick(movieId: Int) {
        val lastAction = viewModel.peekLastAction()
        var direction: NavDirections? = null

        direction = when (lastAction) {
            is FavoritesViewAction.FetchMovies -> {
                FavoritesFragmentDirections.actionFavoriteMoviesFragmentToMovieDetailsFragment(
                    movieId
                )
            }
            is FavoritesViewAction.FetchTvShows -> {
                FavoritesFragmentDirections.actionFavoriteMoviesFragmentToTvShowDetailsFragment(
                    movieId
                )
            }
        }
        findNavController().navigate(direction)
    }

    override fun onCommentsButtonClick(movieId: Int) {
        val action =
            FavoritesFragmentDirections.actionFavoriteMoviesFragmentToReviewFragment(
                movieId
            )
        findNavController().navigate(action)
    }

}