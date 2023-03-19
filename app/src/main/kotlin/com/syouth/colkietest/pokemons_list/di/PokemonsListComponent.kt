package com.syouth.colkietest.pokemons_list.di

import com.syouth.colkietest.base.BaseMapper
import com.syouth.colkietest.pokemons_list.PokemonsListContract
import com.syouth.colkietest.pokemons_list.PokemonsListModel
import com.syouth.colkietest.pokemons_list.StateMapper
import com.syouth.colkietest.pokemons_list.paging.Paginator
import com.syouth.colkietest.pokemons_list.paging.PaginatorImpl
import com.syouth.domain.di.DomainComponent
import com.syouth.nutmegtest.di.UiScope
import dagger.Binds
import dagger.Component
import dagger.Module

@UiScope
@Component(
    dependencies = [
        DomainComponent::class
    ],
    modules = [
        PokemonsListModule::class
    ]
)
internal interface PokemonsListComponent {

    val model: PokemonsListContract.Model

    @Component.Factory
    interface Factory {
        fun create(
            domainComponent: DomainComponent
        ): PokemonsListComponent
    }
}

@Module
internal interface PokemonsListModule {
    @UiScope
    @Binds
    fun bindModel(impl: PokemonsListModel): PokemonsListContract.Model

    @UiScope
    @Binds
    fun bindStateMapper(impl: StateMapper): BaseMapper<PokemonsListContract.DomainState, PokemonsListContract.ViewState>

    @UiScope
    @Binds
    fun bindPaginator(impl: PaginatorImpl): Paginator
}