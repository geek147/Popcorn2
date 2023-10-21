package com.example.popcorn.data.datasource.mapper

import com.example.popcorn.data.datasource.remote.response.GenreResponse
import com.example.popcorn.domain.model.Genre


class GenreRemoteMapper : BaseMapperRepository<GenreResponse.GenreItem, Genre> {
    override fun transform(item: GenreResponse.GenreItem): Genre = Genre(
        id = item.id,
        name = item.name
    )

    override fun transformToRepository(item: Genre): GenreResponse.GenreItem =
        GenreResponse.GenreItem(id = item.id, name = item.name)
}