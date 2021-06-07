package net.smartgekko.moviesearch.viewmodels

import androidx.lifecycle.*
import net.smartgekko.moviesearch.model.database.entities.Movies
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.model.models.Movie
import net.smartgekko.moviesearch.model.repositories.MoviesRepository


class MovieDescriptionViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel(), LifecycleObserver, InetConnectable {
    private var movieId: Long = 0
    private lateinit var movieData: Movie

    override fun initLiveData() {
        MoviesRepository.setLiveData(liveDataToObserve)
    }

    fun getLiveData() = liveDataToObserve

    fun getMovieDetalies() = getDataFromLocalSource()

    fun setIsFavoriteFlag(flag: Boolean) {
        sendIsFavoriteFlag(flag)
    }

    fun setMovieId(id: Long) {
        this.movieId = id
    }

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            fun onMovieDetailsFetched(movie: Movie) {
                liveDataToObserve.postValue(AppState.SuccessDetails(movie))
                movieData = movie
            }

            fun onError() {}

            MoviesRepository.getMovieDetails(
                movieId, onSuccess = ::onMovieDetailsFetched,
                onError = ::onError
            )

        }.start()
    }

    private fun sendIsFavoriteFlag(flag: Boolean) {

        Thread {
            fun onAddToFavorites() {
                liveDataToObserve.postValue(AppState.SuccessSetFavorite(true))
            }

            fun onRemoveFromFavorites() {
                liveDataToObserve.postValue(AppState.SuccessSetFavorite(false))
            }

            fun onError() {}

            when (flag) {
                true -> MoviesRepository.addToFavorites(
                    movieToMovies(movieData), onSuccess = ::onAddToFavorites,
                    onError = ::onError
                )
                else -> MoviesRepository.removeFromFavorites(
                    movieToMovies(movieData), onSuccess = ::onRemoveFromFavorites,
                    onError = ::onError
                )
            }
        }.start()
    }

    private fun movieToMovies(movie: Movie): Movies = Movies(movie)
}