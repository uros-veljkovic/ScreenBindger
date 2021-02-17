package com.example.screenbindger.view.fragment.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.screenbindger.databinding.FragmentReviewBinding
import com.example.screenbindger.util.adapter.recyclerview.ReviewRecyclerViewAdapter
import com.example.screenbindger.util.decorator.ItemReviewDecorator
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.snack
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ReviewFragment : DaggerFragment() {

    @Inject
    lateinit var viewModel: ReviewFragmentViewModel

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private val args: ReviewFragmentArgs by navArgs()
    private val movieId by lazy { args.movieId }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        configRecyclerView()
        fetchReviews()
        observeViewState()
        observeViewAction()
        observeViewEvent()

        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun configRecyclerView() {
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                ItemReviewDecorator(8)
            )
        }
    }

    private fun fetchReviews() {
        viewModel.viewAction.postValue(Event(ReviewFragmentViewAction.FetchReviews))
    }

    private fun observeViewState() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ReviewFragmentViewState.ReviewsFetched -> {
                    requireView().snack("GOOD")
                    binding.rvReviews.apply {
                        adapter = ReviewRecyclerViewAdapter(state.list)
                    }
                }
                is ReviewFragmentViewState.ReviewsNotFetched -> {
                    Toast.makeText(requireContext(), "No reviews found", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeViewAction() {
        viewModel.let {
            it.viewAction.observe(viewLifecycleOwner, Observer { eventWrapper ->
                eventWrapper.getContentIfNotHandled()?.let { action ->
                    when (action) {
                        ReviewFragmentViewAction.FetchReviews -> {
                            it.fetchReviews(movieId)
                        }
                    }
                }
            })
        }
    }

    private fun observeViewEvent() {
        viewModel.also {
            it.viewEvent.observe(viewLifecycleOwner, Observer { eventWrapper ->
                eventWrapper.getContentIfNotHandled()?.let { event ->
                    when (event) {
                        is ReviewFragmentViewEvent.ReviewsFetched -> {
                            val reviews = event.list
                            it.viewState.postValue(ReviewFragmentViewState.ReviewsFetched(reviews))
                            hideProgressBar()
                        }

                        is ReviewFragmentViewEvent.NoReviewsFetched -> {
                            Toast.makeText(requireContext(), "No reviews found", Toast.LENGTH_SHORT)
                                .show()
                            it.viewState.postValue(ReviewFragmentViewState.ReviewsNotFetched)
                            hideProgressBar()
                        }
                        is ReviewFragmentViewEvent.Loading -> {
                            showProgressBar()
                        }
                        is ReviewFragmentViewEvent.Error -> {
                            requireView().snack("Error")
                            showMessage(event.message)
                            hideProgressBar()
                        }
                    }
                }
            })
        }
    }

    private fun hideProgressBar() {

    }

    private fun showProgressBar() {

    }

    private fun showMessage(message: String) {

    }

}