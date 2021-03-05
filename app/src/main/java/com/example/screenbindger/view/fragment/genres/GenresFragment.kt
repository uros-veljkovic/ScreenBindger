package com.example.screenbindger.view.fragment.genres

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.screenbindger.databinding.FragmentGenresBinding
import com.example.screenbindger.util.adapter.recyclerview.ItemGenreRecyclerViewAdapter
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.decorator.GridLayoutRecyclerViewDecorator
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference
import javax.inject.Inject

class GenresFragment : DaggerFragment(), OnCardItemClickListener {

    @Inject
    lateinit var viewModel: GenresViewModel

    private var _binding: FragmentGenresBinding? = null
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
            it.addItemDecoration(GridLayoutRecyclerViewDecorator(2, 32, true))
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

    override fun onCardItemClick(movieId: Int) {
        val genre = viewModel.list?.get(movieId)
        val genreId = genre?.id
        val genreName = genre?.name

        if (genreId != null) {
            val action = GenresFragmentDirections.actionGenresFragmentToGenreMoviesFragment(
                genreName,
                genreId
            )
            findNavController().navigate(action)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}