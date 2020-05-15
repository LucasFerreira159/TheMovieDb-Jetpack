package com.app4funbr.themoviedb.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager

import com.app4funbr.themoviedb.R
import com.app4funbr.themoviedb.view.adapter.MoviesAdapter
import com.app4funbr.themoviedb.viewmodel.ListMoviesViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*

class ListMoviesFragment : Fragment() {

    private lateinit var viewModel: ListMoviesViewModel
    private val moviesAdapter = MoviesAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListMoviesViewModel::class.java)
        viewModel.fetchFromRemote(getString(R.string.API_KEY))

        recycler_movies?.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = moviesAdapter
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.movies.observe(this, Observer { movies ->
            movies?.let {
                recycler_movies?.visibility = View.VISIBLE
                moviesAdapter.updateMovieList(movies)
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                progress?.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    text_recycler_error?.visibility = View.GONE
                    recycler_movies?.visibility = View.GONE
                }
            }
        })

        viewModel.loadError.observe(this, Observer { isError ->
            isError?.let {
                text_recycler_error?.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }
}
