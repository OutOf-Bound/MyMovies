package net.smartgekko.moviesearch.view.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.model.models.Movie
import net.smartgekko.moviesearch.view.adapters.MoviesAdapter
import net.smartgekko.moviesearch.viewmodels.TopMoviesViewModel


private lateinit var viewModel: TopMoviesViewModel

class TopMoviesFragment : Fragment() {
    private lateinit var topMovies: RecyclerView
    private lateinit var topMoviesAdapter: MoviesAdapter

    companion object {
        fun newInstance() = TopMoviesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(
            R.layout.top_movies_fragment, container,
            false
        )
        val activity = activity as Context
        topMovies = view.findViewById(R.id.top_movies)
        topMovies.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        topMoviesAdapter = MoviesAdapter(listOf())
        topMovies.adapter = topMoviesAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TopMoviesViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getMovieList()
        lifecycle.addObserver(viewModel)
    }

    override fun onResume() {
        super.onResume()
        viewModel.initLiveData()
    }

    private fun renderData(appState: AppState) {
        val loadingLayout: FrameLayout?
        loadingLayout = view?.findViewById(R.id.loadingLayoutTop)

        when (appState) {
            is AppState.Success -> {
                val movieData = appState.movieData
                loadingLayout?.visibility = View.GONE
                setData(movieData)
            }
            is AppState.Loading -> {
                loadingLayout?.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout?.visibility = View.GONE
            }
            is AppState.inetConnection -> {
                updateMoviesList(appState.flag)
            }
        }
    }

    private fun setData(movieData: List<Movie>) {
        topMoviesAdapter.updateMovies(movieData)
    }

    private fun updateMoviesList(flag: Boolean) {
        if (flag) {
            viewModel.getMovieList()
        }
    }

}