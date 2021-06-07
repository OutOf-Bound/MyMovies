package net.smartgekko.moviesearch.model.network

import net.smartgekko.moviesearch.model.models.GetMovieDetailResponse
import net.smartgekko.moviesearch.model.models.GetMoviesResponse
import net.smartgekko.moviesearch.utils.tmdbApiKeyV3
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface Api {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = tmdbApiKeyV3,
        @Query("page") page: Int,
        @Query("language") sysLang: String = Locale.getDefault().toLanguageTag()
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopMovies(
        @Query("api_key") apiKey: String = tmdbApiKeyV3,
        @Query("page") page: Int,
        @Query("language") sysLang: String = Locale.getDefault().toLanguageTag()
    ): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = tmdbApiKeyV3,
        @Query("page") page: Int,
        @Query("language") sysLang: String = Locale.getDefault().toLanguageTag()
    ): Call<GetMoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Long = 0,
        @Query("api_key") apiKey: String = tmdbApiKeyV3,
        @Query("language") sysLang: String = Locale.getDefault().toLanguageTag()
    ): Call<GetMovieDetailResponse>
}