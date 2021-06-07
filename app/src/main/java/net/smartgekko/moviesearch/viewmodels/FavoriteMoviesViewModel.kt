package net.smartgekko.moviesearch.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.smartgekko.moviesearch.model.database.entities.Movies
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.model.repositories.MoviesRepository

class FavoriteMoviesViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel(), LifecycleObserver {

    fun getLiveData() = liveDataToObserve

    fun getMovieList() = getDataFromDBSource()

    private fun getDataFromDBSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            fun onFavoriteMoviesFetched(movies: List<Movies>) {
                liveDataToObserve.postValue(AppState.SuccessFavorites(movies))
            }

            fun onError() {}

            MoviesRepository.getFavoriteMovies(
                onSuccess = ::onFavoriteMoviesFetched,
                onError = ::onError
            )
        }.start()
    }
}