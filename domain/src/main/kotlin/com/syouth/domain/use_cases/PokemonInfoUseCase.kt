package com.syouth.domain.use_cases

import com.syouth.domain.entities.PokemonInfo
import kotlinx.coroutines.flow.Flow

interface PokemonInfoUseCase {
    operator fun invoke(pokemonId: Int): Flow<PokemonInfo>
}