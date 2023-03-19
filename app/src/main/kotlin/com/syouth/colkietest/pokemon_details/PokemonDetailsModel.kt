package com.syouth.colkietest.pokemon_details

import com.syouth.colkietest.base.BaseMapper
import com.syouth.colkietest.base.BaseModelImpl
import com.syouth.colkietest.pokemon_details.di.PokemonId
import com.syouth.domain.use_cases.PokemonInfoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class PokemonDetailsModel @Inject constructor(
    stateMapper: BaseMapper<PokemonDetailsContract.DomainState, PokemonDetailsContract.ViewState>,
    private val pokemonInfoUseCase: PokemonInfoUseCase,
    @PokemonId private val pokemonId: Int
) : BaseModelImpl<PokemonDetailsContract.DomainState, PokemonDetailsContract.ViewState, PokemonDetailsContract.Intent>(stateMapper), PokemonDetailsContract.Model {

    private val refreshFlow = MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST).apply {
        tryEmit(Unit)
    }

    override val initialState: PokemonDetailsContract.DomainState get() = PokemonDetailsContract.DomainState.Loading

    override fun processIntent(intent: PokemonDetailsContract.Intent) = when (intent) {
        PokemonDetailsContract.Intent.Refresh -> {
            refreshFlow.tryEmit(Unit)
            Unit
        }
    }

    override fun onShown() {
        super.onShown()
        refreshFlow
            .flatMapConcat {
                pokemonInfoUseCase(pokemonId)
                    .catch {
                        if (currentState is PokemonDetailsContract.DomainState.Loading) {
                            updateState { PokemonDetailsContract.DomainState.Error }
                        }
                    }
            }
            .collectTillHidden { info ->
                updateState {
                    when (it) {
                        PokemonDetailsContract.DomainState.Error -> PokemonDetailsContract.DomainState.PokemonInfoDomainState(info)
                        is PokemonDetailsContract.DomainState.PokemonInfoDomainState -> it.copy(pokemonInfo = info)
                        PokemonDetailsContract.DomainState.Loading -> PokemonDetailsContract.DomainState.PokemonInfoDomainState(info)
                    }
                }
            }
    }
}