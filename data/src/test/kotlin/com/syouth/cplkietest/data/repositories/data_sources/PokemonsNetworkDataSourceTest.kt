package com.syouth.cplkietest.data.repositories.data_sources

import com.syouth.cplkietest.data.models.*
import com.syouth.cplkietest.data.models.mappers.PokemonsMapper
import com.syouth.cplkietest.data.network.PokemonsService
import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage
import com.syouth.domain.entities.Sprites
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner
import java.util.*
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
internal class PokemonsNetworkDataSourceTest {
    private val service: PokemonsService = mock()
    private val mapper: PokemonsMapper = mock()
    private val dataSaver: PokemonsDataSaver = mock()
    private val source = PokemonsNetworkDataSource(
        pokemonsService = service,
        pokemonsMapper = mapper,
        pokemonsDataSaver = dataSaver
    )

    @Test
    fun `GIVEN all data available THEN resulting page is correct and data saved to storage`() = runTest {
        whenever(service.getPokemonsPage(any(), any())).thenReturn(
            flowOf(
                PokemonsPageDto(
                    pokemons = listOf(
                        PokemonPageEntityDto(
                            name = "1",
                            url = "http://p.co/1/"
                        ),
                        PokemonPageEntityDto(
                            name = "2",
                            url = "http://p.co/2/"
                        ),
                        PokemonPageEntityDto(
                            name = "3",
                            url = "http://p.co/3/"
                        ),
                        PokemonPageEntityDto(
                            name = "4",
                            url = "http://p.co/4/"
                        ),
                        PokemonPageEntityDto(
                            name = "5",
                            url = "http://p.co/5/"
                        )
                    )
                )
            )
        )
        val p1 = mockPokemon()
        val p2 = mockPokemon()
        val p3 = mockPokemon()
        val p4 = mockPokemon()
        val p5 = mockPokemon()
        whenever(service.getPokemonInfo(eq(1))).thenReturn(flowOf(p1))
        whenever(service.getPokemonInfo(eq(2))).thenReturn(flowOf(p2))
        whenever(service.getPokemonInfo(eq(3))).thenReturn(flowOf(p3))
        whenever(service.getPokemonInfo(eq(4))).thenReturn(flowOf(p4))
        whenever(service.getPokemonInfo(eq(5))).thenReturn(flowOf(p5))
        val mockedPage = mockPokemonPageWithInfo()
        whenever(mapper.map(any<PokemonsPage>())).thenReturn(mockedPage)
        val pi1 = mockPokemonInfo()
        val pi2 = mockPokemonInfo()
        val pi3 = mockPokemonInfo()
        val pi4 = mockPokemonInfo()
        val pi5 = mockPokemonInfo()
        whenever(mapper.map(eq(p1))).thenReturn(pi1)
        whenever(mapper.map(eq(p2))).thenReturn(pi2)
        whenever(mapper.map(eq(p3))).thenReturn(pi3)
        whenever(mapper.map(eq(p4))).thenReturn(pi4)
        whenever(mapper.map(eq(p5))).thenReturn(pi5)
        whenever(mapper.map(any<PokemonInfo>())).thenReturn(p1)

        val pokemonPage = PokemonsPage(
            pageNumber = 1,
            pokemons = listOf(pi1, pi2, pi3, pi4, pi5)
        )
        val result = source
            .observePage(1)
            .toList()
        Assert.assertEquals(
            listOf(
                pokemonPage
            ),
            result
        )
        verify(dataSaver).savePage(eq(1), eq(mockedPage))
    }

    @Test
    fun `GIVEN pokemon info is observed THEN result is correct and data saved to storage`() = runTest {
        val p1 = mockPokemon()
        val pi1 = mockPokemonInfo()
        whenever(service.getPokemonInfo(eq(1))).thenReturn(flowOf(p1))
        whenever(mapper.map(eq(p1))).thenReturn(pi1)
        whenever(mapper.map(eq(pi1))).thenReturn(p1)

        val result = source
            .observePokemonInfo(1)
            .toList()
        Assert.assertEquals(listOf(pi1), result)
        verify(dataSaver).savePokemonInfo(eq(1), eq(p1))
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