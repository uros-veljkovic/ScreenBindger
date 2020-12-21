package com.example.screenbindger.view.fragment.movie_details

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentMovieDetailsBinding
import com.example.screenbindger.util.adapter.recyclerview.MovieDetailsRecyclerViewAdapter
import com.example.screenbindger.view.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    val viewModel: MovieDetailsViewModel by viewModels()
    private var _binding: FragmentMovieDetailsBinding? = null
    val binding get() = _binding!!

    lateinit var navController: NavController
    val navArgs: MovieDetailsFragmentArgs by navArgs()
    val movieId: Int by lazy { navArgs.movieId }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideActionBar()
    }


    private fun hideActionBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        setupToolbar()
        initRecyclerView()
        fetchData()
        observeServerResponse()
        return view
    }


    private fun setupToolbar() {
        navController = findNavController()
//        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbarTransparent)
        binding.toolbarTransparent.setupWithNavController(navController)

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
                }
                binding.invalidateAll()
            }
        })

        viewModel.responseMovieDetailsCast.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccessful) {
                val items = response.body()?.casts
                (binding.rvMovieDetails.adapter as MovieDetailsRecyclerViewAdapter).addItems(items!!)
            }
            binding.invalidateAll()
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        showActionBar()
        _binding = null
    }

    private fun showActionBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
/*        val activity = (requireActivity() as MainActivity)
        activity.setupToolbar()*/
    }

/*    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profileFragment -> {
                navController.navigate(R.id.action_global_profileFragment)
            }
        }
        return true
    }*/

}