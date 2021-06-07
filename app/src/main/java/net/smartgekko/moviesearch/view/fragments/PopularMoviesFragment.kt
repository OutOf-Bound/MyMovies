package net.smartgekko.moviesearch.view.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.model.models.Movie
import net.smartgekko.moviesearch.utils.MyApplication
import net.smartgekko.moviesearch.utils.SharedPreference
import net.smartgekko.moviesearch.view.adapters.MoviesAdapter
import net.smartgekko.moviesearch.viewmodels.PopularMoviesViewModel


private lateinit var viewModel: PopularMoviesViewModel

class PopularMoviesFragment : Fragment() {
    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private val sharedPreference: SharedPreference = SharedPreference(MyApplication.getAppContext())

    companion object {
        fun newInstance() = PopularMoviesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.popular_movies_fragment, container, false)
        val activity = activity as Context
        popularMovies = view.findViewById(R.id.popular_movies)
        popularMovies.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        popularMoviesAdapter = MoviesAdapter(listOf())
        popularMovies.adapter = popularMoviesAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PopularMoviesViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        lifecycle.addObserver(viewModel)
    }

    override fun onResume() {
        super.onResume()
        viewModel.initLiveData()
        viewModel.getMovieList()
    }

    private fun renderData(appState: AppState) {
        val loadingLayout: FrameLayout? = view?.findViewById(R.id.loadingLayoutPopular)

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
        var tempList: ArrayList<Movie> = ArrayList<Movie>()

        val isAdult = sharedPreference.getValueBoolean(getString(R.string.settings_adult_enable))

        if (!isAdult!!) {
            for (i in movieData as ArrayList) {
                if (!i.adult) {
                    tempList.add(i)
                }
            }
            popularMoviesAdapter.updateMovies(tempList)
        } else {
            popularMoviesAdapter.updateMovies(movieData)
        }
    }

    private fun updateMoviesList(flag: Boolean) {
        if (flag) {
            viewModel.getMovieList()
        }
    }
}