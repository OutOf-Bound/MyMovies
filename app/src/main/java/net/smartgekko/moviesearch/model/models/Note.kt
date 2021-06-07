package net.smartgekko.moviesearch.model.models

import net.smartgekko.moviesearch.model.database.entities.Notes

data class Note(
    val movie_id: Long,
    val stars: Int,
    val note_positive: String,
    val note_negative: String,
    val resume: String
) {
    constructor(note: Notes) : this(
        movie_id = note.movie_id,
        stars = note.stars,
        note_positive = note.note_positive,
        note_negative = note.note_negative,
        resume = note.resume
    )
}