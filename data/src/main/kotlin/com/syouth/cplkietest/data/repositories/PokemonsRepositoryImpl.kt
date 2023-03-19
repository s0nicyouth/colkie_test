package com.syouth.cplkietest.data.repositories

import com.syouth.cplkietest.data.di.LocalSource
import com.syouth.cplkietest.data.di.NetworkSource
import com.syouth.cplkietest.data.repositories.data_sources.PokemonsDataSource
import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage
import com.syouth.domain.repositories.PokemonsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class PokemonsRepositoryImpl @Inject constructor(
    @NetworkSource private val networkSource: PokemonsDataSource,
    @LocalSource private val localSource: PokemonsDataSource
) : PokemonsRepository {
    override fun observePage(pageNumber: Int): Flow<PokemonsPage> =
        orderedDataFlow(localSource.observePage(pageNumber), networkSource.observePage(pageNumber))

    override fun observePokemonInfo(pokemonId: Int): Flow<PokemonInfo> =
        orderedDataFlow(localSource.observePokemonInfo(pokemonId), networkSource.observePokemonInfo(pokemonId))

    // TODO: Simple way to emit Local + Network data with Local data first. Maybe something more configurable should be come up with, but for this purpose it works.
    private fun<T> orderedDataFlow(f: Flow<T>, s: Flow<T>): Flow<T> =
        flow {
            f.lastOrNull()?.let {
                emit(it)
                emitAll(s.catch { })
            } ?: run { emitAll(s) }
        }.flowOn(Dispatchers.IO)
}