package com.syouth.cplkietest.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class SpritesDto(
    @Json(name = "front_default") val front: String?
)