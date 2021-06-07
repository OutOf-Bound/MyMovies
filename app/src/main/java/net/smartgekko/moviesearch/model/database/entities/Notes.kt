package net.smartgekko.moviesearch.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Notes", indices = arrayOf(Index(value = ["movie_id"], unique = true)))
data class Notes(
    @PrimaryKey
    val movie_id: Long,
    val stars: Int,
    val note_positive: String,
    val note_negative: String,
    val resume: String
)