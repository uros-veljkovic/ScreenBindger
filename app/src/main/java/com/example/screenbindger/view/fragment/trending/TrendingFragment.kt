package com.example.screenbindger.view.fragment.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentTrendingBinding
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.adapter.recyclerview.SmallItemMovieRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.decorator.GridLayoutRecyclerViewDecorator
import com.example.screenbindger.util.dialog.SortBy
import com.example.screenbindger.util.dialog.SortDialog
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.*
import com.example.screenbindger.view.fragment.*
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference
import javax.inject.Inject

class TrendingFragment : DaggerFragment(),
    OnCardItemClickListener {

    @Inject
    lateinit var viewModel: TrendingViewModel

    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = bind(inflater, container)
        fetchMovies()
        initRecyclerView()
        initOnClickListeners()

        return view
    }

    private fun fetchMovies() {
        viewModel.executeAction(FetchMovies)
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvTrending.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridLayoutRecyclerViewDecorator(2, 16, true))
            adapter = SmallItemMovieRecyclerViewAdapter(this@TrendingFragment)
        }
    }

    private fun initOnClickListeners() {
        with(binding) {
            tabContainer.tabs.onTabSelected { position ->
                viewModel.tabSelected(position)
            }
            btnNext.setOnClickListener {
                viewModel.executeAction(GotoNextPage)
            }

            btnPrevious.setOnClickListener {
                viewModel.executeAction(GotoPreviousPage)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFragmentState()
    }

    private fun observeFragmentState() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is Fetching -> {
                    showProgressBar()
                }
                is FetchedMovies -> {
                    updateUi(state.list, state.currentPage, state.totalPages)
                }
                is FetchedTvShows -> {
                    updateUi(state.list, state.currentPage, state.totalPages)
                }
                is NotFetched -> {
                    hideProgressBar()
                    showMessage(state.message)
                }
            }
        })
    }

    private fun updateUi(list: List<ShowEntity>, currentPageNumber: Int, totalPages: Int) {
        hideProgressBar()
        configPaginationButtons(currentPageNumber, totalPages)
        populateRecyclerView(list)
    }

    private fun populateRecyclerView(list: List<ShowEntity>) {
        with(binding.rvTrending) {
            if (adapter is SmallItemMovieRecyclerViewAdapter) {
                (adapter as SmallItemMovieRecyclerViewAdapter).setList(list)
            }
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
        viewModel.getDirection(showId).also { direction ->
            findNavController().navigate(direction!!)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        binding.progressBar.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.executeAction(ResetState)
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
        (binding.rvTrending.adapter as SmallItemMovieRecyclerViewAdapter).sort(sort)
    }
}