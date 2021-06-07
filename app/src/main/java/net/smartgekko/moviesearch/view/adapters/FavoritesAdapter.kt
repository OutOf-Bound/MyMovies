package net.smartgekko.moviesearch.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.snackbar.Snackbar
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.model.database.entities.Movies
import net.smartgekko.moviesearch.model.repositories.MoviesRepository
import net.smartgekko.moviesearch.utils.MyApplication
import net.smartgekko.moviesearch.utils.TMDB_PHOTO_URL_W185
import net.smartgekko.moviesearch.view.activities.MovieDescriptionActivity

class FavoritesAdapter(
    private var movies: ArrayList<Movies>
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.movie_list_item_vertical_favorites, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(movies[position])

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(holder.itemView.context, MovieDescriptionActivity::class.java)
            intent.putExtra("movie_id", movies[position].id.toString())
            startActivity(holder.itemView.context, intent, null)
        })

        holder.imgDelete.setOnClickListener(View.OnClickListener {
            fun onSuccess() {
                movies.removeAt(position)
                notifyDataSetChanged()
            }

            fun onError() {
                Snackbar.make(
                    holder.rootLayout,
                    MyApplication.getAppContext().getString(R.string.error_item_delete),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            MoviesRepository.removeFromFavorites(
                movies[position], onSuccess = ::onSuccess,
                onError = ::onError
            )
        })
    }

    fun updateMovies(movies: ArrayList<Movies>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.item_movie_poster)
        private val title: TextView = itemView.findViewById(R.id.item_movie_title)
        private val id: TextView = itemView.findViewById(R.id.item_movie_id)
        private val release_date: TextView = itemView.findViewById(R.id.item_movie_release_date)
        private val overwiew: TextView = itemView.findViewById(R.id.item_movie_overview)
        val imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        val rootLayout: ConstraintLayout = itemView.findViewById(R.id.rootLayout)

        fun bind(movie: Movies) {
            Glide.with(itemView)
                .load(TMDB_PHOTO_URL_W185 + movie.posterPath)
                .transform(CenterCrop())
                .into(poster)

            title.text = movie.title
            id.text = movie.id.toString()
            release_date.text = movie.releaseDate

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