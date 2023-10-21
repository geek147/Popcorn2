package com.example.popcorn.data.datasource.remote.response


import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class GenreResponse(
    @Json(name = "genres")
    val genres: List<GenreItem?>?
) {
    @Keep
    data class GenreItem(
        @Json(name = "id")
        val id: Int,
        @Json(name = "name")
        val name: String
    )
}