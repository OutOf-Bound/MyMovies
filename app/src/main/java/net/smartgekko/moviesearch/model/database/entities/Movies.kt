package net.smartgekko.moviesearch.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import net.smartgekko.moviesearch.model.models.Movie

@Entity(tableName = "Movies", indices = arrayOf(Index(value = ["id"], unique = true)))
data class Movies(

    @PrimaryKey @ColumnInfo(name = "id")
    val id: Long,
    val title: String,
    val overview: String,
    val posterPath: String,
    val rating: Float,
    val releaseDate: String,
    var budget: Long,
    @ColumnInfo(name = "genres")
    val genresString: String,
    @ColumnInfo(name = "productionCompanies")
    val productionCompaniesString: String,
    val status: String,
    val isFavorite: Boolean,
    val adult: Boolean
) {
    constructor(movie: Movie) : this(
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