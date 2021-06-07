package net.smartgekko.moviesearch.model.database.dao

import androidx.room.*
import net.smartgekko.moviesearch.model.database.entities.Movies

@Dao
interface FavoriteMoviesDao {
    @Query("SELECT * FROM Movies")
    fun getAll(): List<Movies>

    @Query("SELECT * FROM Movies WHERE id = :movie_id")
    @OnConflictStrategy()
    fun getById(movie_id: Long): Movies?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteMovies: Movies?)

    @Update
    fun update(favoriteMovies: Movies?)

    @Delete
    fun delete(favoriteMovies: Movies?)
}