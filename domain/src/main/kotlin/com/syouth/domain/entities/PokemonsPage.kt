package com.syouth.domain.entities

data class PokemonsPage(
    val pageNumber: Int,
    val pokemons: List<PokemonInfo>
)
