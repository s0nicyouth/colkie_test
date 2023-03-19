package com.syouth.domain.di

import com.syouth.domain.use_cases.PokemonInfoUseCase
import com.syouth.domain.use_cases.PokemonInfoUseCaseImpl
import com.syouth.domain.use_cases.PokemonsPagesUseCase
import com.syouth.domain.use_cases.PokemonsPagesUseCaseImpl
import com.syouth.nutmegtest.domain.di.DomainScope
import dagger.Binds
import dagger.Component
import dagger.Module

@DomainScope
@Component(
    dependencies = [
        DomainDependencies::class
    ],
    modules = [
        DomainModule::class
    ]
)
interface DomainComponent {

    val pokemonPagesUseCase: PokemonsPagesUseCase
    val pokemonInfoUseCase: PokemonInfoUseCase

    @Component.Factory
    interface Factory {
        fun create(
            domainDependencies: DomainDependencies
        ): DomainComponent
    }
}

@Module
internal interface DomainModule {
    @DomainScope
    @Binds
    fun bindPokemonInfoUseCase(impl: PokemonInfoUseCaseImpl): PokemonInfoUseCase

    @DomainScope
    @Binds
    fun bindPokemonsPageUseCase(impl: PokemonsPagesUseCaseImpl): PokemonsPagesUseCase
}