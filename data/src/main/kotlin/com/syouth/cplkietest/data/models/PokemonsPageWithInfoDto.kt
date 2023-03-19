package com.syouth.cplkietest.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PokemonsPageWithInfoDto(
    @Json(name = "page_number") val pageNumber: Int,
    @Json(name = "pokemons") val pokemons: List<PokemonInfoDto>
)
