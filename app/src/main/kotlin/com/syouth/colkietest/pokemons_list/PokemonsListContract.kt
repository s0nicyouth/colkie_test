package com.syouth.colkietest.pokemons_list

import com.syouth.colkietest.base.*
import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage

internal interface PokemonsListContract {
    interface View : BaseView<ViewState, Intent, Model>

    interface Model : BaseModel<DomainState, ViewState, Intent>

    sealed interface DomainState : BaseDomainState {
        data class PokemonsDomainState(
            val pokemons: List<PokemonsPage>,
            val hasError: Boolean = false
        ) : DomainState
        object ErrorState : DomainState
        object Loading : DomainState
    }

    sealed interface ViewState : BaseViewState {
        data class PokemonsViewState(
            val pokemons: List<PokemonInfo>,
            val lastPage: Int,
            val hasError: Boolean = false
        ) : ViewState
        object ErrorState : ViewState
        object Loading : ViewState
    }

    sealed interface Intent : BaseIntent {
        data class NextPage(val pageNumber: Int) : Intent
        object Refresh : Intent
    }
}