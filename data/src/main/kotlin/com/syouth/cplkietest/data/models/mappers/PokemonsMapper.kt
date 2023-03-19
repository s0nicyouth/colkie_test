package com.syouth.cplkietest.data.models.mappers

import com.syouth.cplkietest.data.models.PokemonInfoDto
import com.syouth.cplkietest.data.models.PokemonsPageWithInfoDto
import com.syouth.domain.entities.PokemonInfo
import com.syouth.domain.entities.PokemonsPage
import com.syouth.kmapper.processor_annotations.Mapper

@Mapper
internal interface PokemonsMapper {
    fun map(dto: PokemonInfoDto): PokemonInfo
    fun map(domain: PokemonInfo): PokemonInfoDto
    fun map(dto: PokemonsPageWithInfoDto): PokemonsPage
    fun map(domain: PokemonsPage): PokemonsPageWithInfoDto
}