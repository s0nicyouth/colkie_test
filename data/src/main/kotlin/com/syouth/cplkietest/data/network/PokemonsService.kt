package com.syouth.cplkietest.data.network

import com.syouth.cplkietest.data.models.PokemonInfoDto
import com.syouth.cplkietest.data.models.PokemonsPageDto
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface PokemonsService {
    @GET("api/v2/pokemon/")
    fun getPokemonsPage(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Flow<PokemonsPageDto>

    @GET("api/v2/pokemon/{id}/")
    fun getPokemonInfo(@Path("id") id: Int): Flow<PokemonInfoDto>
}