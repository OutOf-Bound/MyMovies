package net.smartgekko.moviesearch.viewmodels

import androidx.lifecycle.*
import net.smartgekko.moviesearch.model.models.AppState
import net.smartgekko.moviesearch.model.models.Movie
import net.smartgekko.moviesearch.model.repositories.MoviesRepository
import java.time.LocalDate


class UpcomingMoviesViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel(), LifecycleObserver, InetConnectable {
    private lateinit var moviesList: List<Movie>

    override fun initLiveData() {
        MoviesRepository.setLiveData(liveDataToObserve)
    }

    fun getLiveData() = liveDataToObserve

    fun getMovieList() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {

            fun onUpcomingMoviesFetched(movies: List<Movie>) {
                moviesList = movies.sortedWith(compareBy({ it.releaseDate }))

                val movieUpcomingList = mutableListOf<Movie>()
                for (element in moviesList) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        val dat1 = LocalDate.parse(element.releaseDate)
                        val dat2 = LocalDate.now()

                        if (dat1.isAfter(dat2)) {
                            movieUpcomingList.add(element)
                        }
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                }
                liveDataToObserve.postValue(AppState.Success(movieUpcomingList))
            }

            fun onError() {}

            MoviesRepository.getUpcomingMovies(
                onSuccess = ::onUpcomingMoviesFetched,
                onError = ::onError
            )
        }.start()
    }
}