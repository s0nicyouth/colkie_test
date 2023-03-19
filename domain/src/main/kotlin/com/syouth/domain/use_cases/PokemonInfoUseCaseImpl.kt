package com.syouth.domain.use_cases

import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.repositories.PokemonsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class PokemonInfoUseCaseImpl @Inject constructor(
    private val pokemonsRepository: PokemonsRepository
) : PokemonInfoUseCase {
    override fun invoke(pokemonId: Int): Flow<PokemonInfo> = pokemonsRepository.observePokemonInfo(pokemonId)
}