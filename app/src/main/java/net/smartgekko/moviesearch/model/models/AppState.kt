package net.smartgekko.moviesearch.model.models

import net.smartgekko.moviesearch.model.database.entities.Movies

sealed class AppState {
    data class Success(val movieData: List<Movie>) : AppState()
    data class SuccessDetails(val movie: Movie) : AppState()
    data class SuccessFavorites(val movieFavorites: List<Movies>) : AppState()
    data class SuccessSetFavorite(val flag: Boolean) : AppState()
    data class SuccessNote(val note: Note) : AppState()
    data class SuccessNoteCount(val notesCount: Int) : AppState()
    data class inetConnection(val flag: Boolean) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}