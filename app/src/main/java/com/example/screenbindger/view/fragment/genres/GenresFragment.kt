package com.example.screenbindger.view.fragment.genres

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.screenbindger.databinding.FragmentGenresBinding
import com.example.screenbindger.util.adapter.recyclerview.ItemGenreRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.ItemMovieRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.decorator.GridLayoutRecyclerViewDecorator
import com.example.screenbindger.view.fragment.trending.TrendingFragmentDirections
import com.example.screenbindger.view.fragment.trending.TrendingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class  GenresFragment : Fragment(), OnCardItemClickListener {


    private var _binding: FragmentGenresBinding? = null
    private val binding get() = _binding!!

    val viewModel: GenresViewModel by viewModels()

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
        _binding = FragmentGenresBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvGenres.also {
            it.layoutManager = GridLayoutManager(requireContext(), 2)
            it.addItemDecoration(GridLayoutRecyclerViewDecorator(2, 16, true))
        }
    }

    private fun observeViewModel() {
        viewModel.response.observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.isSuccessful) {
                val list = response.body()?.list?.toMutableList() ?: mutableListOf()
                binding.rvGenres.adapter =
                    ItemGenreRecyclerViewAdapter(WeakReference(requireContext()), this, list)
                binding.rvGenres.startLayoutAnimation()
            }
        })
    }

    override fun onCardItemClick(position: Int) {
        val genre = viewModel.list?.get(position)
        val genreId = genre?.id
        val genreName = genre?.name

        if(genreId != null){
            val action = GenresFragmentDirections.actionGenresFragmentToGenreMoviesFragment(genreName, genreId)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}