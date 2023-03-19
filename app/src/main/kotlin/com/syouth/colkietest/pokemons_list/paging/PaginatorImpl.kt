package com.syouth.colkietest.pokemons_list.paging

import com.syouth.domain.entities.PokemonsPage
import com.syouth.domain.use_cases.PokemonsPagesUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.math.sign

// TODO(): Use jetpack paginator? I have't used it before so just came up with this simple alternative not requiring a lot of boilerplate code. Uses one thread to manipulate data structures so should be thread safe.
@OptIn(ExperimentalCoroutinesApi::class)
internal class PaginatorImpl @Inject constructor(
    private val pokemonsPagesUseCase: PokemonsPagesUseCase
) : Paginator {

    data class PokemonsOrException(
        val pokemons: List<PokemonsPage> = emptyList(),
        val exception: Throwable? = null
    )

    private val pages: MutableList<PokemonsPage> = mutableListOf()
    private val stateFlow = MutableSharedFlow<PokemonsOrException>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val pagesLoading =  mutableSetOf<Int>()
    private val singleThreadDispatcher = Dispatchers.IO.limitedParallelism(1)
    private val singleThreadScope = CoroutineScope(SupervisorJob() + singleThreadDispatcher)

    override fun observePages(): Flow<List<PokemonsPage>> {
        singleThreadScope.coroutineContext.cancelChildren()
        return stateFlow
            .onSubscription { if (pages.isEmpty()) loadPage(1) }
            .onStart {
                //Clear last emission and emit what we've got already
                stateFlow.resetReplayCache()
                if (pages.isNotEmpty()) {
                    stateFlow.emit(PokemonsOrException(pages))
                }
            }.map { pokemonsOrException ->
                pokemonsOrException.exception?.let { throw it } ?: run { pokemonsOrException.pokemons }
            }.flowOn(singleThreadDispatcher)
    }

    override fun loadPage(pageNumber: Int) {
        singleThreadScope.launch {
            try {
                if (pageNumber in pagesLoading || pageNumber in pages.map { it.pageNumber }) return@launch
                pagesLoading += pageNumber
                pokemonsPagesUseCase(pageNumber)
                    .onEach { page ->
                        val index = pages.binarySearch { (it.pageNumber - pageNumber).sign }
                            .let { if (it < 0) -(it + 1) else it }
                        pages.getOrNull(index)?.let {
                            if (it.pageNumber == pageNumber) pages.removeAt(index)
                        }
                        pages.add(index, page)
                        stateFlow.emit(PokemonsOrException(pokemons = pages.toList()))
                    }
                    .onCompletion { pagesLoading -= pageNumber }
                    .catch { e -> stateFlow.emit(PokemonsOrException(exception = e)) }
                    .flowOn(singleThreadDispatcher)
                    .collect()
            } catch (_: CancellationException) {
                pagesLoading.clear()
            }
        }
    }
}