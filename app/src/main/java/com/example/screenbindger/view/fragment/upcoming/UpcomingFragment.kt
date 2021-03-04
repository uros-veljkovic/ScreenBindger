package com.example.screenbindger.view.fragment.upcoming

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentUpcomingBinding
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.adapter.recyclerview.SmallItemMovieRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.decorator.GridLayoutRecyclerViewDecorator
import com.example.screenbindger.util.dialog.SortBy
import com.example.screenbindger.util.dialog.SortDialog
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.*
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference
import javax.inject.Inject


class UpcomingFragment : DaggerFragment(),
    OnCardItemClickListener {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: UpcomingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = bind(inflater, container)
        initRecyclerView()
        initOnClickListeners()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFragmentActions()
        observeFragmentState()
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvUpcoming.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridLayoutRecyclerViewDecorator(2, 16, true))
            adapter = SmallItemMovieRecyclerViewAdapter(this@UpcomingFragment)
        }
    }

    private fun initOnClickListeners() {
        with(binding) {
            tabContainer.tabs.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab!!.position == 0) {
                        viewModel.setAction(UpcomingViewAction.FetchMovies)
                    } else {
                        viewModel.setAction(UpcomingViewAction.FetchTvShows)
                    }
                }

            })

            btnNext.setOnClickListener {
                viewModel.setAction(UpcomingViewAction.GotoNextPage)
            }

            btnPrevious.setOnClickListener {
                viewModel.setAction(UpcomingViewAction.GotoPreviousPage)
            }
        }
    }

    private fun observeFragmentActions() {
        with(viewModel) {
            viewAction.observe(viewLifecycleOwner, Observer { action ->
                when (action) {
                    is UpcomingViewAction.FetchMovies -> {
                        fetchMovies()
                    }
                    is UpcomingViewAction.FetchTvShows -> {
                        fetchTvShows()
                    }
                    is UpcomingViewAction.GotoNextPage -> {
                        val currentState = viewModel.getState()

                        if (currentState is UpcomingViewState.Fetched.Movies)
                            viewModel.nextPageMovies()
                        else
                            viewModel.nextPageTvShows()
                    }
                    is UpcomingViewAction.GotoPreviousPage -> {
                        val currentState = viewModel.getState()

                        if (currentState is UpcomingViewState.Fetched.Movies)
                            viewModel.previousPageMovies()
                        else
                            viewModel.previousPageTvShows()
                    }
                }
            })
        }
    }

    private fun observeFragmentState() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UpcomingViewState.Fetching -> {
                    showProgressBar()
                }
                is UpcomingViewState.Fetched -> {
                    hideProgressBar()
                    configPaginationButtons(state.currentPage, state.totalPages)
                    populateRecyclerView(state.list)
                }
                is UpcomingViewState.NotFetched -> {
                    hideProgressBar()
                    showMessage(state.message)
                }
            }
        })

    }

    private fun populateRecyclerView(list: List<ShowEntity>) {
        with(binding.rvUpcoming) {
            (adapter as SmallItemMovieRecyclerViewAdapter).setList(list)
        }
    }

    private fun configPaginationButtons(currentPageNumber: Int, lastPageNumber: Int) {
        viewModel.currentPage = currentPageNumber
        with(binding) {
            when (currentPageNumber) {
                1 -> {
                    disable(btnPrevious)
                }
                lastPageNumber -> {
                    disable(btnNext)
                }
                else -> {
                    enable(btnNext, btnPrevious)
                }
            }
        }
    }

    private fun showMessage(message: Event<String>) {
        message.getContentIfNotHandled()?.let {
            requireView().snack(it)
        }
    }

    override fun onCardItemClick(showId: Int) {
        val currentState = viewModel.getState()
        val direction: NavDirections?

        direction = when (currentState) {
            is UpcomingViewState.Fetched.Movies -> {
                UpcomingFragmentDirections.actionUpcomingFragmentToMovieDetailsFragment(showId)
            }
            is UpcomingViewState.Fetched.TvShows -> {
                UpcomingFragmentDirections.actionUpcomingFragmentToTvShowDetailsFragment(showId)
            }
            else -> {
                return
            }
        }
        findNavController().navigate(direction)
    }

    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        binding.progressBar.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setAction(UpcomingViewAction.FetchMovies)
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort -> {
                displaySortDialog()
                true
            }
            else -> false
        }
    }

    fun displaySortDialog() {
        SortDialog(WeakReference(requireContext())).showDialog { sort ->
            sortList(sort)
        }
    }

    private fun sortList(sort: SortBy) {
        (binding.rvUpcoming.adapter as SmallItemMovieRecyclerViewAdapter).sort(sort)
    }
}