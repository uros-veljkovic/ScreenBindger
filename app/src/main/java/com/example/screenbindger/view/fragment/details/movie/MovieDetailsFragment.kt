package com.example.screenbindger.view.fragment.details.movie

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentMovieDetailsBinding
import com.example.screenbindger.db.remote.response.movie.trailer.TrailerDetails
import com.example.screenbindger.model.domain.Item
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.MoviePosterUri
import com.example.screenbindger.util.adapter.recyclerview.ShowDetailsRecyclerViewAdapter
import com.example.screenbindger.util.constants.INTENT_ADD_TO_INSTA_STORY
import com.example.screenbindger.util.constants.INTENT_REQUEST_CODE_INSTAGRAM
import com.example.screenbindger.util.constants.POSTER_SIZE_ORIGINAL
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.extensions.snackbar
import com.example.screenbindger.util.image.ImageProvider
import com.example.screenbindger.view.activity.main.MainActivity
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewAction
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewEvent
import com.example.screenbindger.view.fragment.details.ShowDetailsState
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


class MovieDetailsFragment : DaggerFragment(),
    ShowDetailsRecyclerViewAdapter.OnClickListener {

    @Inject
    lateinit var viewModel: MovieDetailsFragmentViewModel

    private val navArgs: MovieDetailsFragmentArgs by navArgs()
    private val movieId: Int by lazy { navArgs.movieId }

    private var _binding: FragmentMovieDetailsBinding? = null
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

        modifyToolbarForFragment()
        initRecyclerView()
        fetchData()
        observeViewModelState()
        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    private fun modifyToolbarForFragment() {
        (activity as MainActivity).modifyToolbarForFragment()
    }

    private fun initRecyclerView() {
        binding.rvMovieDetails.apply {
            adapter = ShowDetailsRecyclerViewAdapter(this@MovieDetailsFragment)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun fetchData() {
        viewModel.fetchData(movieId)
    }

    private fun observeViewModelState() {
        viewModel.viewState.eventState.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { state ->
                when (state) {
                    is ShowDetailsState.Fetching -> {
                        showProgressBar()
                    }
                    is ShowDetailsState.FetchedAll -> {
                        val movie = viewModel.viewState.show as Item
                        val casts = viewModel.viewState.casts as List<Item>
                        val list = mutableListOf<Item>().apply {
                            add(movie)
                            addAll(casts)
                        }
                        populateList(list)
                        fetchTrailers()

                    }
                    is ShowDetailsState.Error -> {
                        snackbar(state.message)
                    }
                    is ShowDetailsState.NoDataAvailable -> {
                        snackbar("No data available", R.color.logout_red)
                    }
                    else -> {

                    }
                }
            }

        })
    }

    fun populateList(list: List<Item>) {
        with(binding) {
            val adapter = rvMovieDetails.adapter as ShowDetailsRecyclerViewAdapter
            adapter.addItems(list)
            rvMovieDetails.startLayoutAnimation()
            executePendingBindings()
            hideProgressBar()
        }
    }

    private fun fetchTrailers() {
        viewModel.setAction(DetailsFragmentViewAction.FetchTrailers)
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
                    is DetailsFragmentViewAction.MarkAsFavorite -> {
                        viewModel.markAsFavorite(true, action.id)
                    }
                    is DetailsFragmentViewAction.MarkAsNotFavorite -> {
                        viewModel.markAsFavorite(false, action.id)
                    }
                    is DetailsFragmentViewAction.FetchTrailers -> {
                        viewModel.fetchTrailers(movieId)
                    }
                    DetailsFragmentViewAction.WatchTrailer -> {
                        showTrailer(viewModel.trailer)
                    }
                }
            }
        })
    }

    private fun observeViewModelEvents() {
        viewModel.viewEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    DetailsFragmentViewEvent.IsLoadedAsFavorite -> {
                        hideProgressBar()
                        animateFabToFavorite()
                    }
                    DetailsFragmentViewEvent.IsLoadedAsNotFavorite -> {
                        hideProgressBar()
                        animateFabToNotFavorite()
                    }
                    is DetailsFragmentViewEvent.TrailersFetched -> {
                        hideProgressBar()
                        val firstVideo = it.trailers[0]
                        viewModel.trailer = firstVideo
                    }
                    is DetailsFragmentViewEvent.TrailersNotFetched -> {
                        hideProgressBar()
                        snackbar("SHIT")
                        hideTrailerButton()
                    }
                    is DetailsFragmentViewEvent.AddedToFavorites -> {
                        hideProgressBar()
                        snackbar(it.message, R.color.green)
                        animateFabToFavorite()
                    }
                    is DetailsFragmentViewEvent.RemovedFromFavorites -> {
                        hideProgressBar()
                        animateFabToNotFavorite()
                    }
                    is DetailsFragmentViewEvent.Error -> {
                        hideProgressBar()
                        snackbar(it.message)
                    }
                    is DetailsFragmentViewEvent.Rest -> {
                        hideProgressBar()
                    }
                    is DetailsFragmentViewEvent.Loading -> {
                        showProgressBar()
                    }
                    is DetailsFragmentViewEvent.PosterSaved -> {
                        val socialMediaCode = it.socialMediaRequestCode
                        pickImageForShare(socialMediaCode)
                    }
                    is DetailsFragmentViewEvent.PosterNotSaved -> {
                        snackbar("not saved !", R.color.logout_red)
                    }
                }
            }
        })
    }

    private fun hideTrailerButton() {
        val adapter = binding.rvMovieDetails.adapter as ShowDetailsRecyclerViewAdapter
        adapter.hideTrailerIcon()

    }

    private fun pickImageForShare(socialNetworkCode: Int) {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        ).apply {
            startActivityForResult(this, socialNetworkCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                when (requestCode) {
                    INTENT_REQUEST_CODE_INSTAGRAM -> {
                        shareInstaStory(uri)
                    }
                }
            }

        }
    }

    private fun showTrailer(video: TrailerDetails?) {

        video?.let {
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
        }?:viewModel.setEvent(DetailsFragmentViewEvent.Error("Error loading trailer"))
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

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.reset()
        modifyToolbarForActivity()
        animateFabToNotFavorite()
        _binding = null
    }

    private fun modifyToolbarForActivity() {
        (activity as MainActivity).modifyToolbarForActivity()
    }

    override fun onBtnWatchTrailer() {
        viewModel.viewAction.postValue(Event(DetailsFragmentViewAction.WatchTrailer))
    }

    override fun onBtnShareToInstagram(movieEntity: ShowEntity) {
        fetchPoster(movieEntity) { bitmap ->
            viewModel.saveToGalleryForInstagram(bitmap, requireContext(), "Posters")
        }
    }

    private fun fetchPoster(movieEntity: ShowEntity, callback: (Bitmap) -> Unit) {
        if (verifyPermissions().not()) {
            return
        }

        val uri = MoviePosterUri.Builder
            .withPosterPath(movieEntity.smallPosterUrl!!)
            .withPosterSize(POSTER_SIZE_ORIGINAL)
            .build()

        CoroutineScope(IO).launch {
            ImageProvider.download(uri, requireContext())?.also { bitmap ->
                callback(bitmap)
            }
        }
    }

    private fun verifyPermissions(): Boolean {

        val isGrantedPermission =
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

        if (isGrantedPermission != PackageManager.PERMISSION_GRANTED) {
            val permissions =
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            ActivityCompat.requestPermissions(requireActivity(), permissions, 1)
            return false
        }
        return true
    }

    private fun shareInstaStory(uri: Uri) {

        val intent = Intent(INTENT_ADD_TO_INSTA_STORY).apply {
            setDataAndType(uri, "image/jpeg")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        requireActivity().let {
            if (it.packageManager.resolveActivity(intent, 0) != null) {
                it.startActivityForResult(intent, 0)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }


}