package com.example.popcorn.data.datasource.mapper

interface BaseMapperRepository<E, D> {

    fun transform(item: E): D

    fun transformToRepository(item: D): E
}
