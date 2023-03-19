package com.syouth.cplkietest.data.repositories.data_sources

import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage
import kotlinx.coroutines.flow.Flow

internal interface PokemonsDataSource {
    fun observePage(pageNumber: Int): Flow<PokemonsPage>
    fun observePokemonInfo(pokemonId: Int): Flow<PokemonInfo>
}