package com.syouth.domain.di

import com.syouth.domain.repositories.PokemonsRepository

interface DomainDependencies {
    val pokemonsRepository: PokemonsRepository
}