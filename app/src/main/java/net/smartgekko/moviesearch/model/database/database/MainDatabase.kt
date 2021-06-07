package net.smartgekko.moviesearch.model.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.smartgekko.moviesearch.model.database.dao.FavoriteMoviesDao
import net.smartgekko.moviesearch.model.database.dao.NotesDao
import net.smartgekko.moviesearch.model.database.entities.Movies
import net.smartgekko.moviesearch.model.database.entities.Notes

@Database(entities = arrayOf(Movies::class, Notes::class), version = 1)
abstract class MainDatabase : RoomDatabase() {
    abstract fun favoriteMoviesDao(): FavoriteMoviesDao?
    abstract fun notesDao(): NotesDao?
}