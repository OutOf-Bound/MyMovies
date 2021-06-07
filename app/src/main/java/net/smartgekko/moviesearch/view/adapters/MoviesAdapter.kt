package net.smartgekko.moviesearch.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.model.models.Movie
import net.smartgekko.moviesearch.utils.TMDB_PHOTO_URL_W185
import net.smartgekko.moviesearch.view.activities.MovieDescriptionActivity

class MoviesAdapter(private var movies: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.movie_list_item_vertical, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(holder.itemView.context, MovieDescriptionActivity::class.java)
            intent.putExtra("movie_id", movies[position].id.toString())
            startActivity(holder.itemView.context, intent, null)
        })
    }

    fun updateMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView = itemView.findViewById(R.id.item_movie_poster)
        private val title: TextView = itemView.findViewById(R.id.item_movie_title)
        private val id: TextView = itemView.findViewById(R.id.item_movie_id)
        private val release_date: TextView = itemView.findViewById(R.id.item_movie_release_date)
        private val rating: TextView = itemView.findViewById(R.id.item_movie_rating)
        private val overwiew: TextView = itemView.findViewById(R.id.item_movie_overview)

        fun bind(movie: Movie) {
            Glide.with(itemView)
                .load(TMDB_PHOTO_URL_W185 + movie.posterPath)
                .transform(CenterCrop())
                .into(poster)

            title.text = movie.title
            id.text = movie.id.toString()
            release_date.text = movie.releaseDate
            rating.text = movie.rating.toString()

            val string_limit = 250
            val overview_string: String = movie.overview

            if (overview_string.length < string_limit) {
                overwiew.text = overview_string
            } else {
                overwiew.text = overview_string.substring(0, string_limit) + "..."
            }
        }
    }
}