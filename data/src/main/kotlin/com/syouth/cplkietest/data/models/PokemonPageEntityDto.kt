package com.syouth.cplkietest.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PokemonPageEntityDto(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)
