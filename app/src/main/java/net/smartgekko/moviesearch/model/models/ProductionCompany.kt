package net.smartgekko.moviesearch.model.models

import com.google.gson.annotations.SerializedName

class ProductionCompany(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("origin_country") val origin_country: String
)