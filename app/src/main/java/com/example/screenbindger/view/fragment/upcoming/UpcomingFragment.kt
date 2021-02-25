package com.example.screenbindger.view.fragment.upcoming

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentUpcomingBinding
import com.example.screenbindger.model.domain.movie.MovieEntity
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.adapter.recyclerview.SmallItemMovieRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.decorator.GridLayoutRecyclerViewDecorator
import com.example.screenbindger.util.dialog.SortBy
import com.example.screenbindger.util.dialog.SortDialog
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.extensions.snack
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference
import javax.inject.Inject


class UpcomingFragment : DaggerFragment(),
    OnCardItemClickListener {

    var _binding: FragmentUpcomingBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var viewModel: UpcomingFragmentViewModel

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
        observeFragmentActions()
        observeFragmentState()
        return view
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

    private fun observeFragmentActions() {
        with(viewModel) {
            viewAction.observe(viewLifecycleOwner, Observer { action ->
                when (action) {
                    UpcomingFragmentViewAction.FetchMovies -> {
                        fetchMovies()
                    }
                    UpcomingFragmentViewAction.FetchTvShows -> {
                        fetchTvShows()
                    }
                }
            })
        }
    }

    private fun observeFragmentState() {
        viewModel.upcomingViewState.observe(viewLifecycleOwner, Observer { response ->
            when (response.state) {
                is ListState.Init -> {
                    viewModel.fetchMovies()
                }
                is ListState.Fetching -> {
                    showProgressBar()
                }
                is ListState.Fetched -> {
                    hideProgressBar()
                    populateRecyclerView(response.list!!)
                }
                is ListState.NotFetched -> {
                    hideProgressBar()
                    showMessage(response.state.message)
                }
            }
        })

    }

    private fun populateRecyclerView(list: List<MovieEntity>) {
        with(binding.rvUpcoming) {
            (adapter as SmallItemMovieRecyclerViewAdapter).setList(list)
            startLayoutAnimation()
        }
    }

    private fun showMessage(message: Event<String>) {
        message.getContentIfNotHandled()?.let {
            requireView().snack(it)
        }
    }

    override fun onCardItemClick(movieId: Int) {
        val action =
            UpcomingFragmentDirections.actionUpcomingFragmentToMovieDetailsFragment(movieId)
        findNavController().navigate(action)
    }

    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        binding.progressBar.hide()
    }

    override fun onResume() {
        super.onResume()

        initOnClickListeners()
    }

    private fun initOnClickListeners() {
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == 0) {
                    viewModel.fetchMovies()
                } else {
                    viewModel.fetchTvShows()
                }
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

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