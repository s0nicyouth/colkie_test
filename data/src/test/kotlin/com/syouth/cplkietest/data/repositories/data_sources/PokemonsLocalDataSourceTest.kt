package com.syouth.cplkietest.data.repositories.data_sources

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.syouth.cplkietest.data.models.PokemonInfoDto
import com.syouth.cplkietest.data.models.PokemonsPageWithInfoDto
import com.syouth.cplkietest.data.models.SpritesDto
import com.syouth.cplkietest.data.models.mappers.PokemonsMapper
import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage
import com.syouth.domain.entities.Sprites
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.random.Random

internal class PokemonsLocalDataSourceTest {
    private val prefs: SharedPreferences = mock()
    private val moshi: Moshi = Moshi.Builder().build()
    private val pokemonsMapper: PokemonsMapper = mock()
    private val source = PokemonsLocalDataSource(
        sharedPreferences = prefs,
        moshi = moshi,
        pokemonsMapper = pokemonsMapper
    )

    @Test
    fun `GIVEN pages observed WHEN no data available THEN nothing is emited`() = runTest {
        whenever(prefs.getString(any(), anyOrNull())).thenReturn(null)
        val result = source
            .observePage(1)
            .toList()
        Assert.assertEquals(emptyList<PokemonsPage>(), result)
    }

    @Test
    fun `GIVEN pages observed WHEN data available THEN nothing is emited`() = runTest {
        val page = mockPokemonPageWithInfo()
        val domainPage = mockPokemonsPage()
        val pageString = moshi.adapter(PokemonsPageWithInfoDto::class.java).toJson(page)
        whenever(prefs.getString(any(), anyOrNull())).thenReturn(pageString)
        whenever(pokemonsMapper.map(any<PokemonsPageWithInfoDto>())).thenReturn(domainPage)
        val result = source
            .observePage(1)
            .toList()
        Assert.assertEquals(listOf(domainPage), result)
    }

    @Test
    fun `GIVEN info observed WHEN no data available THEN nothing is emited`() = runTest {
        whenever(prefs.getString(any(), anyOrNull())).thenReturn(null)
        val result = source
            .observePokemonInfo(1)
            .toList()
        Assert.assertEquals(emptyList<PokemonInfo>(), result)
    }

    @Test
    fun `GIVEN info observed WHEN data available THEN nothing is emited`() = runTest {
        val info = mockPokemon()
        val infoDomain = mockPokemonInfo()
        val pageString = moshi.adapter(PokemonInfoDto::class.java).toJson(info)
        whenever(prefs.getString(any(), anyOrNull())).thenReturn(pageString)
        whenever(pokemonsMapper.map(any<PokemonInfoDto>())).thenReturn(infoDomain)
        val result = source
            .observePokemonInfo(1)
            .toList()
        Assert.assertEquals(listOf(infoDomain), result)
    }
}

private fun mockPokemon(): PokemonInfoDto = PokemonInfoDto(
    id = Random.nextInt(),
    name = UUID.randomUUID().toString(),
    height = Random.nextInt(),
    weight = Random.nextInt(),
    sprites = SpritesDto("")
)

private fun mockPokemonInfo() = PokemonInfo(
    id = Random.nextInt(),
    name = UUID.randomUUID().toString(),
    height = Random.nextInt(),
    weight = Random.nextInt(),
    sprites = Sprites("")
)

private fun mockPokemonPageWithInfo(): PokemonsPageWithInfoDto = PokemonsPageWithInfoDto(
    pageNumber = 1,
    pokemons = emptyList()
)

private fun mockPokemonsPage() = PokemonsPage(
    pageNumber = 1,
    pokemons = emptyList()
)