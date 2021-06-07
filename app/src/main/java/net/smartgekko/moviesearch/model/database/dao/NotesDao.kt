package net.smartgekko.moviesearch.model.database.dao

import androidx.room.*
import net.smartgekko.moviesearch.model.database.entities.Notes
import net.smartgekko.moviesearch.model.models.Note

@Dao
interface NotesDao {
    @Query("SELECT * FROM Notes")
    fun getNotesAll(): List<Notes>

    @Query("SELECT * FROM Notes WHERE movie_id = :movie_id")
    fun getNotesById(movie_id: Long): Note?

    @Query("SELECT count(movie_id) FROM Notes WHERE movie_id = :movie_id")
    fun getNotesCountById(movie_id: Long): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Notes?)

    @Update
    fun update(note: Notes?)

    @Delete
    fun delete(note: Notes?)
}