package com.syouth.cplkietest.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PokemonsPageDto(
    @Json(name = "results") val pokemons: List<PokemonPageEntityDto>
)
