package net.smartgekko.moviesearch.model.models

import com.google.gson.annotations.SerializedName

data class GetMovieDetailResponse
    (
    @SerializedName("id") var id: Long,
    @SerializedName("budget") var budget: Long,
    @SerializedName("genres") var genres: List<Genre>,
    @SerializedName("overview") var overview: String,
    @SerializedName("poster_path") var posterPath: String,
    @SerializedName("production_companies") var production_companies: List<ProductionCompany>,
    @SerializedName("release_date") var releaseDate: String,
    @SerializedName("status") var status: String,
    @SerializedName("title") var title: String,
    @SerializedName("adult") var adult: Boolean

)
