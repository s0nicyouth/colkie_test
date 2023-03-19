package com.syouth.domain.repositories

import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage
import kotlinx.coroutines.flow.Flow

interface PokemonsRepository {
    fun observePage(pageNumber: Int): Flow<PokemonsPage>
    fun observePokemonInfo(pokemonId: Int): Flow<PokemonInfo>
}