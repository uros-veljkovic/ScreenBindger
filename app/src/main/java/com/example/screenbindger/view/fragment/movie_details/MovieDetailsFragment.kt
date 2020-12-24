package com.example.screenbindger.view.fragment.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentMovieDetailsBinding
import com.example.screenbindger.util.adapter.recyclerview.MovieDetailsRecyclerViewAdapter
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    val viewModel: MovieDetailsViewModel by viewModels()
    private var _binding: FragmentMovieDetailsBinding? = null
    val binding get() = _binding!!

    lateinit var navController: NavController
    val navArgs: MovieDetailsFragmentArgs by navArgs()
    val movieId: Int by lazy { navArgs.movieId }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        setHasOptionsMenu(true)

        modifyToolbarForFragment()
        initRecyclerView()
        fetchData()
        observeServerResponse()
        return view
    }

    private fun modifyToolbarForFragment() {

        val activity = (requireActivity() as MainActivity)
        val transparentBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.background_toolbar)
        activity.toolbar.background = (transparentBackground)

        val constraintLayout = activity.container

        ConstraintSet().also {

            it.clone(constraintLayout)

            it.connect(
                activity.nav_host_fragment_activity_main.id,
                ConstraintSet.TOP,
                activity.toolbar.id,
                ConstraintSet.TOP,
                0
            )
            it.applyTo(constraintLayout)
        }

    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvMovieDetails.apply {
            adapter = MovieDetailsRecyclerViewAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun fetchData() {
        viewModel.fetchData(movieId)
    }

    private fun observeServerResponse() {
        viewModel.responseMovieDetails.observe(viewLifecycleOwner, Observer { response ->
            if (response?.body() != null && response.isSuccessful) {
                val movieEntity = response.body()
                if (movieEntity != null) {
                    (binding.rvMovieDetails.adapter as MovieDetailsRecyclerViewAdapter).addItems(
                        listOf(
                            movieEntity
                        )
                    )
                    binding.rvMovieDetails.startLayoutAnimation()
                }
//                binding.invalidateAll()
            }
        })

        viewModel.responseMovieDetailsCast.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccessful) {
                val items = response.body()?.casts
                if (!items.isNullOrEmpty())
                    (binding.rvMovieDetails.adapter as MovieDetailsRecyclerViewAdapter).addItems(
                        items
                    )
            }
            binding.invalidateAll()
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        viewModel.reset()
        modifyToolbarForActivity()
    }

    private fun modifyToolbarForActivity() {

        val activity = (requireActivity() as MainActivity)
        val transparentBackground =
            ContextCompat.getColor(requireContext(), R.color.defaultBackground)
        activity.toolbar.setBackgroundColor(transparentBackground)

        val constraintLayout = activity.container

        ConstraintSet().also {

            it.clone(constraintLayout)

            it.connect(
                activity.nav_host_fragment_activity_main.id,
                ConstraintSet.TOP,
                activity.toolbar.id,
                ConstraintSet.BOTTOM,
                0
            )
            it.applyTo(constraintLayout)
        }


    }


}