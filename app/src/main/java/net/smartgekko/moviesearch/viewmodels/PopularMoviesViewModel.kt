package net.smartgekko.moviesearch.viewmodels

import androidx.lifecycle.*
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.model.models.Movie
import net.smartgekko.moviesearch.model.repositories.MoviesRepository


class PopularMoviesViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel(), LifecycleObserver, InetConnectable {

    override fun initLiveData() {
        MoviesRepository.setLiveData(liveDataToObserve)
    }

    fun getLiveData() = liveDataToObserve

    fun getMovieList() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {

            fun onPopularMoviesFetched(movies: List<Movie>) {
                liveDataToObserve.postValue(AppState.Success(movies))
            }

            fun onError() {}

            MoviesRepository.getPopularMovies(
                onSuccess = ::onPopularMoviesFetched,
                onError = ::onError
            )
        }.start()
    }
}