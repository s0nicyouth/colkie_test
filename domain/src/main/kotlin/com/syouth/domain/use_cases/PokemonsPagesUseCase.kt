package com.syouth.domain.use_cases

import com.syouth.domain.entities.PokemonsPage
import kotlinx.coroutines.flow.Flow

interface PokemonsPagesUseCase {
    operator fun invoke(numberOfPages: Int): Flow<PokemonsPage>
}