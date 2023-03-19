package com.syouth.colkietest.pokemon_details

import com.syouth.colkietest.base.BaseMapper
import javax.inject.Inject

internal class StateMapper @Inject constructor(

) : BaseMapper<PokemonDetailsContract.DomainState, PokemonDetailsContract.ViewState> {
    override fun invoke(from: PokemonDetailsContract.DomainState): PokemonDetailsContract.ViewState = when (from) {
        PokemonDetailsContract.DomainState.Error -> PokemonDetailsContract.ViewState.Error
        PokemonDetailsContract.DomainState.Loading -> PokemonDetailsContract.ViewState.Loading
        is PokemonDetailsContract.DomainState.PokemonInfoDomainState -> PokemonDetailsContract.ViewState.PokemonInfoViewState(from.pokemonInfo)
    }
}