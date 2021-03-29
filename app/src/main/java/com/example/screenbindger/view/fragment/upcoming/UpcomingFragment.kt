package com.example.screenbindger.view.fragment.upcoming

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
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
import com.example.screenbindger.view.fragment.*
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
        fetchMovies()
        initRecyclerView()
        initOnClickListeners()
        return view
    }

    private fun fetchMovies() {
        viewModel.executeAction(FetchMovies)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            tabContainer.tabs.onTabSelected { position ->
                viewModel.tabSelected(position)
            }
            pagingContainer.btnNext.setOnClickListener {
                viewModel.executeAction(GotoNextPage)
            }
            pagingContainer.btnPrevious.setOnClickListener {
                viewModel.executeAction(GotoPreviousPage)
            }
        }
    }

    private fun observeFragmentState() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ShowListViewState.Fetching -> {
                    showProgressBar()
                }
                is ShowListViewState.FetchedMovies -> {
                    hideProgressBar()
                    updateUi(state.list, state.currentPage, state.totalPages)
                }
                is ShowListViewState.FetchedTvShows -> {
                    hideProgressBar()
                    updateUi(state.list, state.currentPage, state.totalPages)
                }
                is ShowListViewState.NotFetched -> {
                    hideProgressBar()
                    showMessage(state.message)
                }
            }
        })

    }

    private fun updateUi(list: List<ShowEntity>, currentPage: Int, totalPages: Int) {
        hideProgressBar()
        configPaginationButtons(currentPage, totalPages)
        populateRecyclerView(list)
    }

    private fun populateRecyclerView(list: List<ShowEntity>) {
        with(binding.rvUpcoming) {
            (adapter as SmallItemMovieRecyclerViewAdapter).setList(list)
        }
    }

    private fun configPaginationButtons(currentPageNumber: Int, lastPageNumber: Int) {
        viewModel.currentPage = currentPageNumber
        setCurrentAndLastPage(currentPageNumber, lastPageNumber)
        with(binding.pagingContainer) {
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

    private fun setCurrentAndLastPage(currentPageNumber: Int, lastPageNumber: Int) {
        with(binding.pagingContainer) {
            tvPageCurrent.text = currentPageNumber.toString()
            tvPageLast.text = lastPageNumber.toString()
        }
    }

    private fun showMessage(message: Event<String>) {
        message.getContentIfNotHandled()?.let {
            requireView().snack(it)
        }
    }

    override fun onCardItemClick(showId: Int) {
        viewModel.getNavDirection(showId).also {
            findNavController().navigate(it!!)
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
        (binding.rvUpcoming.adapter as SmallItemMovieRecyclerViewAdapter).sort(sort)
    }
}