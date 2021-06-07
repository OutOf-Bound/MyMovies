package net.smartgekko.moviesearch.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.databinding.ActivityFavoritesBinding
import net.smartgekko.moviesearch.model.database.entities.Movies
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.view.adapters.FavoritesAdapter
import net.smartgekko.moviesearch.viewmodels.FavoriteMoviesViewModel

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var favoriteMovies: RecyclerView
    private lateinit var favoriteMoviesAdapter: FavoritesAdapter
    private lateinit var viewModel: FavoriteMoviesViewModel

    override fun onBackPressed() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteMovies = binding.favoriteMovies
        favoriteMovies.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        favoriteMoviesAdapter = FavoritesAdapter(arrayListOf())
        favoriteMovies.adapter = favoriteMoviesAdapter

        viewModel = ViewModelProvider(this).get(FavoriteMoviesViewModel::class.java)
        viewModel.getLiveData().observe(this, { renderData(it) })

        lifecycle.addObserver(viewModel)

        val topAppBar = binding.topAppBar
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    goHome()
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getMovieList()
    }

    private fun renderData(appState: AppState) {
        val loadingLayout: FrameLayout?
        loadingLayout = binding.loadingLayoutFavorites

        when (appState) {
            is AppState.SuccessFavorites -> {
                val movieData = appState.movieFavorites as ArrayList
                loadingLayout.visibility = View.GONE
                setData(movieData)
            }
            is AppState.Loading -> {
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
            }
            else -> {
            }
        }
    }

    private fun setData(movieData: ArrayList<Movies>) {
        favoriteMoviesAdapter.updateMovies(movieData)
    }

    private fun goSearch() {
        val intent = Intent(applicationContext, SearchActivity::class.java)
        startActivity(intent)
    }

    fun goHome() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
    }
}