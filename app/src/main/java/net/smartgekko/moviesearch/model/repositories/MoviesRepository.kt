package net.smartgekko.moviesearch.model.repositories


import androidx.lifecycle.MutableLiveData
import net.smartgekko.moviesearch.model.database.dao.FavoriteMoviesDao
import net.smartgekko.moviesearch.model.database.dao.NotesDao
import net.smartgekko.moviesearch.model.database.entities.Movies
import net.smartgekko.moviesearch.model.database.entities.Notes
import net.smartgekko.moviesearch.model.models.*
import net.smartgekko.moviesearch.model.network.Api
import net.smartgekko.moviesearch.utils.MyApplication
import net.smartgekko.moviesearch.utils.TMDB_BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path

object MoviesRepository {
    private val api: Api
    private var liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    private var favoriteMoviesDao: FavoriteMoviesDao
    private var notesDao: NotesDao

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(Api::class.java)
        val db = MyApplication.getDatabase()
        favoriteMoviesDao = db?.favoriteMoviesDao()!!
        notesDao = db?.notesDao()!!
    }

    fun setLiveData(lData: MutableLiveData<AppState>) {
        liveData = lData
    }

    fun updateConnectionInfo(connInfo: Boolean) {
        liveData.postValue(AppState.inetConnection(connInfo))
    }

    fun getPopularMovies(
        page: Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        api.getPopularMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getTopMovies(
        page: Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        api.getTopMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getUpcomingMovies(
        page: Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        api.getUpcomingMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getMovieDetails(
        @Path("id") id: Long,
        onSuccess: (movie: Movie) -> Unit,
        onError: () -> Unit
    ) {
        api.getMovieDetails(id = id)
            .enqueue(object : Callback<GetMovieDetailResponse> {
                override fun onResponse(
                    call: Call<GetMovieDetailResponse>,
                    response: Response<GetMovieDetailResponse>
                ) {

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        var productionCompanyString = ""
                        var genresString = ""

                        if (responseBody != null) {
                            var isInFavorites = false

                            if (getFavoriteById(responseBody.id) != null) isInFavorites = true

                            for (index in responseBody.production_companies.indices) {
                                if (index < responseBody.production_companies.size - 1) {
                                    productionCompanyString += responseBody.production_companies[index].name + ". "
                                }
                            }

                            for (index in responseBody.genres.indices) {
                                if (index < responseBody.genres.size - 1) {
                                    genresString += responseBody.genres[index].name + "\n"
                                }
                            }

                            val movie = Movie(
                                responseBody.id,
                                responseBody.title,
                                responseBody.overview,
                                responseBody.posterPath,
                                0.0f,
                                responseBody.releaseDate,
                                responseBody.budget,
                                genresString,
                                productionCompanyString,
                                responseBody.status,
                                isInFavorites,
                                responseBody.adult
                            )

                            onSuccess.invoke(movie)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMovieDetailResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getFavoriteMovies(
        onSuccess: (movies: List<Movies>) -> Unit,
        onError: () -> Unit
    ) {
        try {
            onSuccess.invoke(favoriteMoviesDao.getAll())
        } catch (e: Exception) {
            onError.invoke()
        }
    }

    fun getFavoriteById(id: Long?): Movies? {
        return favoriteMoviesDao.getById(id!!)
    }

    fun addToFavorites(
        movie: Movies?,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        try {
            favoriteMoviesDao.insert(movie)
            onSuccess.invoke()
        } catch (e: Exception) {
            onError.invoke()
        }
    }

    fun removeFromFavorites(
        movie: Movies?,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        try {
            favoriteMoviesDao.delete(movie)
            onSuccess.invoke()
        } catch (e: Exception) {
            onError.invoke()
        }
    }

    fun addToNotes(
        note: Notes?,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        try {
            notesDao.insert(note)
            onSuccess.invoke()
        } catch (e: Exception) {
            onError.invoke()
        }
    }

    fun removeFromNotes(
        note: Notes?,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        try {
            notesDao.delete(note)
            onSuccess.invoke()
        } catch (e: Exception) {
            onError.invoke()
        }
    }

    fun getNoteById(id: Long?): Note? {
        var note: Note = Note(0, 0, "", "", "")
        try {
            note = notesDao.getNotesById(id!!)!!
        } catch (e: Exception) {
        }
        return note
    }

    fun getNotesCountById(id: Long?): Int? {
        return notesDao.getNotesCountById(id!!)
    }

}
