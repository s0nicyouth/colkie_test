package com.syouth.cplkietest.data.repositories

import com.syouth.cplkietest.data.repositories.data_sources.PokemonsDataSource
import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage
import com.syouth.domain.entities.Sprites
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class PokemonsRepositoryImplTest {
    private val networkSource: PokemonsDataSource = mock()
    private val localSource: PokemonsDataSource = mock()
    private val repository = PokemonsRepositoryImpl(
        networkSource = networkSource,
        localSource = localSource
    )

    @Test
    fun `GIVEN no local source pokemon info data THEN only network source data is emitted`() = runTest {
        val mockedPokemodData = PokemonInfo(
            id = 1,
            name = "",
            height = 1,
            weight = 1,
            sprites = Sprites("")
        )
        whenever(localSource.observePokemonInfo(any())).thenReturn(emptyFlow())
        whenever(networkSource.observePokemonInfo(any())).thenReturn(flowOf(mockedPokemodData))
        val result = repository
            .observePokemonInfo(1)
            .toList()
        Assert.assertEquals(listOf(mockedPokemodData), result)
    }

    @Test
    fun `GIVEN local source pokemon info data available THEN all data is emitted`() = runTest {
        val mockedLocalData = PokemonInfo(
            id = 1,
            name = "",
            height = 1,
            weight = 1,
            sprites = Sprites("")
        )
        val mockednetworkData = PokemonInfo(
            id = 1,
            name = "",
            height = 1,
            weight = 1,
            sprites = Sprites("")
        )
        whenever(localSource.observePokemonInfo(any())).thenReturn(flowOf(mockedLocalData))
        whenever(networkSource.observePokemonInfo(any())).thenReturn(flowOf(mockednetworkData))
        val result = repository
            .observePokemonInfo(1)
            .toList()
        Assert.assertEquals(listOf(mockedLocalData, mockednetworkData), result)
    }

    @Test
    fun `GIVEN no network source pokemon info data THEN only local source data is emitted`() = runTest {
        val mockedPokemodData = PokemonInfo(
            id = 1,
            name = "",
            height = 1,
            weight = 1,
            sprites = Sprites("")
        )
        whenever(localSource.observePokemonInfo(any())).thenReturn(flowOf(mockedPokemodData))
        whenever(networkSource.observePokemonInfo(any())).thenReturn(emptyFlow())
        val result = repository
            .observePokemonInfo(1)
            .toList()
        Assert.assertEquals(listOf(mockedPokemodData), result)
    }

    @Test
    fun `GIVEN no local source page info data THEN only network source data is emitted`() = runTest {
        val mockedPageData = PokemonsPage(
            pageNumber = 1,
            pokemons = emptyList()
        )
        whenever(localSource.observePage(any())).thenReturn(emptyFlow())
        whenever(networkSource.observePage(any())).thenReturn(flowOf(mockedPageData))
        val result = repository
            .observePage(1)
            .toList()
        Assert.assertEquals(listOf(mockedPageData), result)
    }

    @Test
    fun `GIVEN local source page info data available THEN all data is emitted`() = runTest {
        val mockedLocalData = PokemonsPage(
            pageNumber = 1,
            pokemons = emptyList()
        )
        val mockednetworkData = PokemonsPage(
            pageNumber = 1,
            pokemons = emptyList()
        )
        whenever(localSource.observePage(any())).thenReturn(flowOf(mockedLocalData))
        whenever(networkSource.observePage(any())).thenReturn(flowOf(mockednetworkData))
        val result = repository
            .observePage(1)
            .toList()
        Assert.assertEquals(listOf(mockedLocalData, mockednetworkData), result)
    }

    @Test
    fun `GIVEN no network source page info data THEN only local source data is emitted`() = runTest {
        val mockedPageData = PokemonsPage(
            pageNumber = 1,
            pokemons = emptyList()
        )
        whenever(localSource.observePage(any())).thenReturn(flowOf(mockedPageData))
        whenever(networkSource.observePage(any())).thenReturn(emptyFlow())
        val result = repository
            .observePage(1)
            .toList()
        Assert.assertEquals(listOf(mockedPageData), result)
    }
}