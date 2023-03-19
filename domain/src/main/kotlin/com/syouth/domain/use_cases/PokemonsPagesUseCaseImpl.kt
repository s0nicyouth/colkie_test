package com.syouth.domain.use_cases

import com.syouth.domain.entities.PokemonsPage
import com.syouth.domain.repositories.PokemonsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class PokemonsPagesUseCaseImpl @Inject constructor(
    private val pokemonsRepository: PokemonsRepository
) : PokemonsPagesUseCase {
    override fun invoke(numberOfPages: Int): Flow<PokemonsPage> = pokemonsRepository.observePage(numberOfPages)
}