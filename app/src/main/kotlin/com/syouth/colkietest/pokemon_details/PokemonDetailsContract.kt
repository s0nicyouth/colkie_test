package com.syouth.colkietest.pokemon_details

import com.syouth.colkietest.base.*
import com.syouth.domain.entities.PokemonInfo

internal interface PokemonDetailsContract {

    companion object {
        const val POKEMON_ID = "POKEMON_ID"
    }

    interface View : BaseView<ViewState, Intent, Model>

    interface Model : BaseModel<DomainState, ViewState, Intent>

    sealed interface DomainState : BaseDomainState {
        data class PokemonInfoDomainState(
            val pokemonInfo: PokemonInfo
        ) : DomainState
        object Error : DomainState
        object Loading : DomainState
    }

    sealed interface ViewState : BaseViewState {
        data class PokemonInfoViewState(
            val pokemonInfo: PokemonInfo
        ) : ViewState
        object Error : ViewState
        object Loading : ViewState
    }

    sealed interface Intent : BaseIntent {
        object Refresh : Intent
    }
}