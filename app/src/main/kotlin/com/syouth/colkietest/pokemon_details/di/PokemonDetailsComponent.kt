package com.syouth.colkietest.pokemon_details.di

import com.syouth.colkietest.base.BaseMapper
import com.syouth.colkietest.pokemon_details.PokemonDetailsContract
import com.syouth.colkietest.pokemon_details.PokemonDetailsModel
import com.syouth.colkietest.pokemon_details.StateMapper
import com.syouth.domain.di.DomainComponent
import com.syouth.nutmegtest.di.UiScope
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@UiScope
@Component(
    dependencies = [
        DomainComponent::class
    ],
    modules = [
        PokemonDetailsModule::class
    ]
)
internal interface PokemonDetailsComponent {

    val model: PokemonDetailsContract.Model

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @PokemonId pokemonId: Int,
            domainComponent: DomainComponent
        ): PokemonDetailsComponent
    }
}

@Module
internal interface PokemonDetailsModule {

    @UiScope
    @Binds
    fun bindModel(impl: PokemonDetailsModel): PokemonDetailsContract.Model

    @UiScope
    @Binds
    fun bindStateMapper(impl: StateMapper): BaseMapper<PokemonDetailsContract.DomainState, PokemonDetailsContract.ViewState>
}