package com.syouth.cplkietest.data.repositories.data_sources

import android.net.Uri
import com.syouth.cplkietest.data.models.mappers.PokemonsMapper
import com.syouth.cplkietest.data.network.PokemonsService
import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 20

// TODO: Internet connection detection should be used to make requests only when network is available. Separate helper class is required for this.
@OptIn(ExperimentalCoroutinesApi::class)
internal class PokemonsNetworkDataSource @Inject constructor(
    private val pokemonsService: PokemonsService,
    private val pokemonsMapper: PokemonsMapper,
    private val pokemonsDataSaver: PokemonsDataSaver
) : PokemonsDataSource {
    override fun observePage(pageNumber: Int): Flow<PokemonsPage> =
        pokemonsService
            .getPokemonsPage((pageNumber - 1) * ITEMS_PER_PAGE, ITEMS_PER_PAGE)
            .flatMapLatest { dto ->
                dto
                    .pokemons
                    .asFlow()
                    .flatMapConcat { observePokemonInfo(getPokemonIdFromUrl(it.url)) }
                    .scan(listOf<PokemonInfo>()) { l, next ->
                        mutableListOf<PokemonInfo>().apply {
                            addAll(l)
                            add(next)
                        }
                    }.filter { it.size == dto.pokemons.size }
                    .map {
                        PokemonsPage(
                            pageNumber = pageNumber,
                            pokemons = it
                        )
                    }
            }.onEach { pokemonsDataSaver.savePage(pageNumber, pokemonsMapper.map(it)) }
            .flowOn(Dispatchers.IO)

    override fun observePokemonInfo(pokemonId: Int): Flow<PokemonInfo> =
        pokemonsService
            .getPokemonInfo(pokemonId)
            .map(pokemonsMapper::map)
            .onEach { pokemonsDataSaver.savePokemonInfo(pokemonId, pokemonsMapper.map(it)) }
            .flowOn(Dispatchers.IO)

    private fun getPokemonIdFromUrl(url: String): Int =
        Uri.parse(url).pathSegments.last().toInt()
}