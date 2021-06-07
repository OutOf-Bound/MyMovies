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
import net.smartgekko.moviesearch.viewmodels.UpcomingMoviesViewModel


private lateinit var viewModel: UpcomingMoviesViewModel

class UpcomingMoviesFragment : Fragment() {
    private lateinit var upcomingMovies: RecyclerView
    private lateinit var upcomingMoviesAdapter: MoviesAdapter

    companion object {
        fun newInstance() = UpcomingMoviesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.upcoming_movies_fragment, container,
            false
        )
        val activity = activity as Context

        upcomingMovies = view.findViewById(R.id.upcoming_movies)
        upcomingMovies.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        upcomingMoviesAdapter = MoviesAdapter(listOf())
        upcomingMovies.adapter = upcomingMoviesAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpcomingMoviesViewModel::class.java)
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
        loadingLayout = view?.findViewById(R.id.loadingLayoutUpcoming)
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
                val error = appState.error
            }
            is AppState.inetConnection -> {
                updateMoviesList(appState.flag)
            }
        }
    }

    private fun setData(movieData: List<Movie>) {
        upcomingMoviesAdapter.updateMovies(movieData)
    }

    private fun updateMoviesList(flag: Boolean) {
        if (flag) {
            viewModel.getMovieList()
        }
    }
}