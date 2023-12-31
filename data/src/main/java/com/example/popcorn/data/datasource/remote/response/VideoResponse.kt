package com.example.popcorn.data.datasource.remote.response


import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class VideoResponse(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "results")
    val videos: List<MovieVideoItem?>?
) {
    @Keep
    data class MovieVideoItem(
        @Json(name = "id")
        val id: String,
        @Json(name = "iso_3166_1")
        val iso31661: String?,
        @Json(name = "iso_639_1")
        val iso6391: String?,
        @Json(name = "key")
        val key: String?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "official")
        val official: Boolean?,
        @Json(name = "published_at")
        val publishedAt: String?,
        @Json(name = "site")
        val site: String?,
        @Json(name = "size")
        val size: Int?,
        @Json(name = "type")
        val type: String?
    )
}