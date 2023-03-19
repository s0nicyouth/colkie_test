package com.syouth.colkietest.pokemons_list

import com.syouth.colkietest.base.BaseMapper
import javax.inject.Inject

internal class StateMapper @Inject constructor(

) : BaseMapper<PokemonsListContract.DomainState, PokemonsListContract.ViewState> {
    override fun invoke(from: PokemonsListContract.DomainState): PokemonsListContract.ViewState = when (from) {
        PokemonsListContract.DomainState.ErrorState -> PokemonsListContract.ViewState.ErrorState
        is PokemonsListContract.DomainState.PokemonsDomainState -> PokemonsListContract.ViewState.PokemonsViewState(
            pokemons = from.pokemons.flatMap { it.pokemons },
            lastPage = from.pokemons.lastOrNull()?.pageNumber ?: 0,
            hasError = from.hasError
        )
        PokemonsListContract.DomainState.Loading -> PokemonsListContract.ViewState.Loading
    }
}