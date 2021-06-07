package net.smartgekko.moviesearch.model.models

import com.google.gson.annotations.SerializedName
import net.smartgekko.moviesearch.model.database.entities.Movies


data class Movie(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("budget") val budget: Long,
    val genresString: String,
    val productionCompaniesString: String,
    @SerializedName("status") val status: String,
    val isFavorite: Boolean,
    @SerializedName("adult") val adult: Boolean
) {
    constructor(movie: Movies) : this(
        id = movie.id,
        title = movie.title,
        overview = movie.overview,
        posterPath = movie.posterPath,
        rating = movie.rating,
        releaseDate = movie.releaseDate,
        budget = movie.budget,
        genresString = movie.genresString,
        productionCompaniesString = movie.productionCompaniesString,
        status = movie.status,
        isFavorite = movie.isFavorite,
        adult = movie.adult
    )
}


