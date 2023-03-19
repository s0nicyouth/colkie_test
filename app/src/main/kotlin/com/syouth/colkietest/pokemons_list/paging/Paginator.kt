package com.syouth.colkietest.pokemons_list.paging

import com.syouth.domain.entities.PokemonsPage
import kotlinx.coroutines.flow.Flow

internal interface Paginator {
    fun observePages(): Flow<List<PokemonsPage>>
    fun loadPage(pageNumber: Int)
}