package com.syouth.cplkietest.data.repositories.data_sources

import com.syouth.cplkietest.data.models.PokemonInfoDto
import com.syouth.cplkietest.data.models.PokemonsPageWithInfoDto

internal interface PokemonsDataSaver {
    fun savePage(pageNumber: Int, page: PokemonsPageWithInfoDto)
    fun savePokemonInfo(pokemonId: Int, info: PokemonInfoDto)
}