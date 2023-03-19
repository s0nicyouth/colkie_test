package com.syouth.colkietest.pokemons_list

import com.syouth.colkietest.base.BaseMapper
import com.syouth.colkietest.base.BaseModelImpl
import com.syouth.colkietest.pokemons_list.paging.Paginator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class PokemonsListModel @Inject constructor(
    stateMapper: BaseMapper<PokemonsListContract.DomainState, PokemonsListContract.ViewState>,
    private val paginator: Paginator
) : BaseModelImpl<PokemonsListContract.DomainState, PokemonsListContract.ViewState, PokemonsListContract.Intent>(stateMapper), PokemonsListContract.Model {
    override val initialState: PokemonsListContract.DomainState get() = PokemonsListContract.DomainState.Loading
    private val refreshFlow = MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST).apply {
        tryEmit(Unit)
    }

    override fun onShown() {
        refreshFlow
            .flatMapLatest {
                paginator
                    .observePages()
                    .catch {
                        // Errors like lack of the internet connection should be handled separately on Data level to automatically continue loading once internet is back.
                        // Currently manual refreshing is required to start loading again if internet connection got broken.
                        updateState {
                            when (it) {
                                PokemonsListContract.DomainState.ErrorState -> it
                                PokemonsListContract.DomainState.Loading -> PokemonsListContract.DomainState.ErrorState
                                is PokemonsListContract.DomainState.PokemonsDomainState -> it.copy(hasError = true)
                            }
                        }
                    }
            }
            .collectTillHidden { pages ->
                updateState {
                    when (it) {
                        PokemonsListContract.DomainState.ErrorState -> PokemonsListContract.DomainState.PokemonsDomainState(pages)
                        is PokemonsListContract.DomainState.PokemonsDomainState -> it.copy(pokemons = pages, hasError = false)
                        PokemonsListContract.DomainState.Loading -> PokemonsListContract.DomainState.PokemonsDomainState(pages)
                    }
                }
            }
    }

    override fun processIntent(intent: PokemonsListContract.Intent) = when (intent) {
        is PokemonsListContract.Intent.NextPage -> paginator.loadPage(intent.pageNumber)
        PokemonsListContract.Intent.Refresh -> {
            updateState { PokemonsListContract.DomainState.Loading }
            refreshFlow.tryEmit(Unit)
            Unit
        }
    }
}