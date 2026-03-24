package com.example.flixyConnect.data.model


import kotlinx.serialization.Serializable
import com.example.flixyConnect.data.model.ProductionCompany

@Serializable
data class MovieDetails (
    val adult: Boolean,
    val backdrop_path: String? = null,
    val budget: Int? = null,
    val genres: List<Genre>? = null,
    val homepage: String? = null,
    val id: Int,
    val imdb_id: String? = null,
    val original_language: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    val production_companies: List<ProductionCompany>? = null,
    val release_date: String? = null,
    val revenue: Int? = null,
    val runtime: Int? = null,
    val status: String? = null,
    val tagline: String? = null,
    val title: String,
    val video: Boolean? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null
) {
}