package net.smartgekko.moviesearch.view.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.databinding.ActivityMovieDescriptionBinding
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.model.models.Movie
import net.smartgekko.moviesearch.utils.AppBroadcastReceiver
import net.smartgekko.moviesearch.utils.INTENT_ACTION_KEY
import net.smartgekko.moviesearch.utils.NUM_PAGES_DETAIL
import net.smartgekko.moviesearch.utils.TMDB_PHOTO_URL_W500
import net.smartgekko.moviesearch.view.fragments.MyNotesFragment
import net.smartgekko.moviesearch.view.fragments.PopularMoviesFragment
import net.smartgekko.moviesearch.view.fragments.TopMoviesFragment
import net.smartgekko.moviesearch.view.fragments.UpcomingMoviesFragment
import net.smartgekko.moviesearch.viewmodels.MovieDescriptionViewModel

private lateinit var viewModel: MovieDescriptionViewModel

class MovieDescriptionActivity : AppCompatActivity() {
    override fun onBackPressed() = finish()

    private lateinit var viewPager: ViewPager2
    private val receiver = AppBroadcastReceiver()
    private lateinit var binding: ActivityMovieDescriptionBinding
    lateinit var movieId: String
    private lateinit var movieData: Movie
    private var isFavoriteFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.close -> {
                    finish()
                    true
                }
                else -> false
            }
        }

        viewPager = binding.viewPager2
        val pagerAdapter = FragmentSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        movieId = intent.extras?.getString("movie_id").toString()
        viewModel = ViewModelProvider(this).get(MovieDescriptionViewModel::class.java)
        viewModel.getLiveData().observe(this, { renderData(it) })// подписались на LiveData.AppState
        viewModel.setMovieId(movieId.toLong())
        viewModel.getMovieDetalies()
        lifecycle.addObserver(viewModel)
    }

    override fun onResume() {
        super.onResume()
        viewModel.initLiveData()
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        filter.addAction(INTENT_ACTION_KEY)
        registerReceiver(receiver, filter)
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
        }
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.SuccessDetails -> {
                loadingLayout.visibility = View.GONE
                movieData = appState.movie
                setData(movieData)
            }
            is AppState.SuccessSetFavorite -> {
                loadingLayout.visibility = View.GONE
                isFavoriteFlag = appState.flag
                setFavorite(isFavoriteFlag)
            }
            is AppState.Loading -> {
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
            }
            is AppState.inetConnection -> {
                updateMoviesList(appState.flag)
            }
        }
    }


    private fun setFavorite(flag: Boolean) {

        when (flag) {
            true -> {
                binding.buttonRemoveFromFavoritesL.visibility = View.VISIBLE
                binding.buttonAddToFavoritesL.visibility = View.GONE
                Snackbar.make(
                    binding.constraintLayout,
                    resources.getString(R.string.movie_add_to_favorites),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            else -> {
                binding.buttonAddToFavoritesL.visibility = View.VISIBLE
                binding.buttonRemoveFromFavoritesL.visibility = View.GONE
                Snackbar.make(
                    binding.constraintLayout,
                    resources.getString(R.string.movie_deleted_from_favorites),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setData(movieData: Movie) {
        Glide.with(this)
            .load(TMDB_PHOTO_URL_W500 + movieData.posterPath)
            .transform(CenterCrop())
            .into(binding.moviePoster)
        binding.movieTitle.setText(movieData.title)
        binding.movieBudget.setText(movieData.budget.toString())
        binding.movieStatus.setText(movieData.status)
        binding.movieOverview.setText(movieData.overview)
        binding.movieReleaseDate.setText(movieData.releaseDate)
        binding.movieProduction.append(movieData.productionCompaniesString)
        binding.movieGenres.append(movieData.genresString)

        when (movieData.isFavorite) {
            true -> {
                binding.buttonRemoveFromFavoritesL.visibility = View.VISIBLE
                binding.buttonAddToFavoritesL.visibility = View.GONE

            }
            else -> {
                binding.buttonAddToFavoritesL.visibility = View.VISIBLE
                binding.buttonRemoveFromFavoritesL.visibility = View.GONE
            }
        }
    }

    fun addToFavorites(view: View) {
        viewModel.setIsFavoriteFlag(true)
    }

    fun removeFromFavorites(view: View) {
        viewModel.setIsFavoriteFlag(false)
    }

    private fun updateMoviesList(flag: Boolean) {
        if (flag) {
            viewModel.getMovieDetalies()
        }
    }

    private inner class FragmentSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES_DETAIL
        override fun createFragment(position: Int): Fragment {

            return when (position) {
                0 -> {
                    MyNotesFragment.newInstance(movieId.toLong())

                }
                else -> MyNotesFragment.newInstance(movieId.toLong())
            }
        }
    }
}