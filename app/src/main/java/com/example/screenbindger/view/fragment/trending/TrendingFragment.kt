package com.example.screenbindger.view.fragment.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.screenbindger.databinding.FragmentTrendingBinding
import com.example.screenbindger.model.domain.movie.MovieEntity
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.adapter.recyclerview.SmallItemMovieRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.decorator.GridLayoutRecyclerViewDecorator
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.extensions.snack
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TrendingFragment : DaggerFragment(),
    OnCardItemClickListener {

    @Inject
    lateinit var viewModel: TrendingViewModel

    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = bind(inflater, container)
        initRecyclerView()
        observeViewModel()
        return view
    }
    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvTrending.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridLayoutRecyclerViewDecorator(2, 16, true))
            adapter = SmallItemMovieRecyclerViewAdapter(this@TrendingFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.trendingViewState.observe(viewLifecycleOwner, Observer { response ->
            when (response.state) {
                is ListState.Init -> {
                    viewModel.fetchData()
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
        with(binding.rvTrending) {
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
            TrendingFragmentDirections.actionTrendingFragmentToMovieDetailsFragment(movieId)
        findNavController().navigate(action)
    }

    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        binding.progressBar.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}