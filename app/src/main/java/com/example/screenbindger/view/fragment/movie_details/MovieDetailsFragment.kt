package com.example.screenbindger.view.fragment.movie_details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentMovieDetailsBinding
import com.example.screenbindger.db.remote.response.movie.trailer.MovieTrailerDetails
import com.example.screenbindger.model.domain.Item
import com.example.screenbindger.util.adapter.recyclerview.MovieDetailsRecyclerViewAdapter
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.extensions.snack
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MovieDetailsFragment : DaggerFragment(),
    MovieDetailsRecyclerViewAdapter.OnClickListener {

    @Inject
    lateinit var viewModel: MovieDetailsFragmentViewModel

    private val navArgs: MovieDetailsFragmentArgs by navArgs()
    private val movieId: Int by lazy { navArgs.movieId }

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        setHasOptionsMenu(true)

        modifyToolbarForFragment()
        initOnClickListeners()
        initRecyclerView()
        fetchData()
        observeViewModelState()
        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
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

    private fun initOnClickListeners() {
        binding.btnAddOrRemoveAsFavorite.setOnClickListener {

            with(viewModel) {
                val event = viewEvent.value?.peekContent()
                if (event != null &&
                    (event is MovieDetailsFragmentViewEvent.IsLoadedAsFavorite ||
                            event is MovieDetailsFragmentViewEvent.AddedToFavorites)

                )
                    markAsFavorite(false, movieId)
                else
                    markAsFavorite(true, movieId)
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvMovieDetails.apply {
            adapter = MovieDetailsRecyclerViewAdapter(this@MovieDetailsFragment)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun fetchData() {
        viewModel.fetchData(movieId)
    }

    private fun observeViewModelState() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.eventState.getContentIfNotHandled()?.let { state ->
                when (state) {
                    MovieDetailsState.Fetching -> {
                        showProgressBar()
                    }
                    MovieDetailsState.MovieFetched -> {
                        val movie = it.movie
                        populateList(listOf(movie as Item))
                        hideProgressBar()
                    }
                    MovieDetailsState.CastsFetched -> {
                        val list = it.casts
                        populateList(list as List<Item>)
                    }
                    is MovieDetailsState.Error.MovieNotFetched -> {
                        showError(state)
                    }
                    is MovieDetailsState.Error.CastsNotFetched -> {
                        showError(state)
                    }
                }
            }

        })
    }

    fun populateList(list: List<Item>) {
        with(binding) {
            val adapter = rvMovieDetails.adapter as MovieDetailsRecyclerViewAdapter
            adapter.addItems(list)
            rvMovieDetails.startLayoutAnimation()
            invalidateAll()
        }
    }

    private fun showError(state: MovieDetailsState.Error) {
        requireView().snack(state.message)
    }

    private fun showError(message: String) {
        requireView().snack(message, R.color.logout_red)
    }

    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        binding.progressBar.hide()
    }

    override fun onResume() {
        super.onResume()

        observeViewModelAction()
        observeViewModelEvents()
    }

    private fun observeViewModelAction() {
        viewModel.viewAction.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { action ->
                when (action) {
                    is MovieDetailsFragmentViewAction.MarkAsFavorite -> {
                        viewModel.markAsFavorite(true, action.movieID)
                    }
                    is MovieDetailsFragmentViewAction.MarkAsNotFavorite -> {
                        viewModel.markAsFavorite(false, action.movieID)
                    }
                    is MovieDetailsFragmentViewAction.FetchTrailers -> {
                        viewModel.fetchTrailers(movieId)
                    }
                }
            }
        })
    }

    private fun observeViewModelEvents() {
        viewModel.viewEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    MovieDetailsFragmentViewEvent.IsLoadedAsFavorite -> {
                        hideProgressBar()
                        animateFabToFavorite()
                    }
                    MovieDetailsFragmentViewEvent.IsLoadedAsNotFavorite -> {
                        hideProgressBar()
                        animateFabToNotFavorite()
                    }
                    is MovieDetailsFragmentViewEvent.TrailersFetched -> {
                        hideProgressBar()
                        val firstVideo = it.trailers[0]
                        showTrailer(firstVideo)
                    }
                    is MovieDetailsFragmentViewEvent.TrailersNotFetched -> {
                        hideProgressBar()
                        showMessage("No trailer found")
                    }
                    is MovieDetailsFragmentViewEvent.AddedToFavorites -> {
                        hideProgressBar()
                        showMessage(it.message, R.color.green)
                        animateFabToFavorite()
                    }
                    is MovieDetailsFragmentViewEvent.RemovedFromFavorites -> {
                        hideProgressBar()
                        animateFabToNotFavorite()
                    }
                    is MovieDetailsFragmentViewEvent.Error -> {
                        hideProgressBar()
                        showError(it.message)

                    }
                    is MovieDetailsFragmentViewEvent.Rest -> {
                        hideProgressBar()
                    }
                    is MovieDetailsFragmentViewEvent.Loading -> {
                        showProgressBar()
                    }
                }
            }
        })
    }

    private fun showTrailer(video: MovieTrailerDetails) {
        val videoKey = video.key
        val url = "https://youtube.com/watch?v=$videoKey"

        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoKey"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )

        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
    }

    private fun animateFabToFavorite() {
        with(binding) {
            btnAddOrRemoveAsFavorite.animate()
                .scaleY(1.2f)
                .scaleX(1.2f)
                .setDuration(500)
                .withEndAction {
                    binding.btnAddOrRemoveAsFavorite.setColorFilter(Color.RED)
                    binding.btnAddOrRemoveAsFavorite.refreshDrawableState()
                }

        }
    }

    private fun animateFabToNotFavorite() {
        with(binding) {
            btnAddOrRemoveAsFavorite.animate()
                .scaleY(1f)
                .scaleX(1f)
                .setDuration(500)
                .withEndAction {
                    binding.btnAddOrRemoveAsFavorite.setColorFilter(Color.WHITE)
                    binding.btnAddOrRemoveAsFavorite.refreshDrawableState()
                }

        }
    }

    private fun paintFabTo(message: String, colorRes: Int) {
        requireView().snack(message, colorRes)
    }

    private fun showMessage(message: String) {
        requireView().snack(message)
    }

    private fun showMessage(message: String, colorRes: Int = R.color.defaultBackground) {
        requireView().snack(message, colorRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.reset()
        modifyToolbarForActivity()
        animateFabToNotFavorite()
        _binding = null
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

    override fun onButtonWatchTrailerClick() {
        viewModel.viewAction.postValue(Event(MovieDetailsFragmentViewAction.FetchTrailers))
    }


}