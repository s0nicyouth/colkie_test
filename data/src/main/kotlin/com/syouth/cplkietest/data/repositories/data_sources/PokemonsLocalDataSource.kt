package com.syouth.cplkietest.data.repositories.data_sources

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.syouth.cplkietest.data.models.PokemonInfoDto
import com.syouth.cplkietest.data.models.PokemonsPageWithInfoDto
import com.syouth.cplkietest.data.models.mappers.PokemonsMapper
import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Those source just emit data they already have. Ideally continuous emission should be used.
// TODO: Very simple local storage. Some serious storage should be used e.g. SQLite(Room)
internal class PokemonsLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val moshi: Moshi,
    private val pokemonsMapper: PokemonsMapper
) : PokemonsDataSource, PokemonsDataSaver {

    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun observePage(pageNumber: Int): Flow<PokemonsPage> = flow {
        sharedPreferences.getString(pageNumberKey(pageNumber), null)?.let { json ->
            moshi.adapter(PokemonsPageWithInfoDto::class.java).fromJson(json)?.let(pokemonsMapper::map)?.let {
                emit(it)
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun observePokemonInfo(pokemonId: Int): Flow<PokemonInfo> = flow {
        sharedPreferences.getString(pokemonIdKey(pokemonId), null)?.let { json ->
            moshi.adapter(PokemonInfoDto::class.java).fromJson(json)?.let(pokemonsMapper::map)?.let {
                emit(it)
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun savePage(pageNumber: Int, page: PokemonsPageWithInfoDto) {
        ioScope.launch {
            sharedPreferences
                .edit()
                .putString(
                    pageNumberKey(pageNumber),
                    moshi.adapter(PokemonsPageWithInfoDto::class.java).toJson(page)
                ).apply()
        }
    }

    override fun savePokemonInfo(pokemonId: Int, info: PokemonInfoDto) {
        ioScope.launch {
            sharedPreferences
                .edit()
                .putString(pokemonIdKey(pokemonId), moshi.adapter(PokemonInfoDto::class.java).toJson(info))
                .apply()
        }
    }

    private fun pageNumberKey(pageNumber: Int) = "page_number_$pageNumber"
    private fun pokemonIdKey(pokemonId: Int) = "pokemon_id_$pokemonId"
}