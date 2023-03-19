package com.syouth.cplkietest.data.di

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.syouth.cplkietest.data.models.mappers.PokemonsMapper
import com.syouth.cplkietest.data.models.mappers.PokemonsMapperImpl
import com.syouth.cplkietest.data.network.PokemonsService
import com.syouth.cplkietest.data.repositories.PokemonsRepositoryImpl
import com.syouth.cplkietest.data.repositories.data_sources.PokemonsDataSaver
import com.syouth.cplkietest.data.repositories.data_sources.PokemonsDataSource
import com.syouth.cplkietest.data.repositories.data_sources.PokemonsLocalDataSource
import com.syouth.cplkietest.data.repositories.data_sources.PokemonsNetworkDataSource
import com.syouth.domain.di.DomainDependencies
import com.syouth.domain.repositories.PokemonsRepository
import com.syouth.nutmegtest.data.di.DataScope
import com.syouth.nutmegtest.di.ApplicationContext
import dagger.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tech.thdev.network.flowcalladapterfactory.FlowCallAdapterFactory

@DataScope
@Component(
    modules = [
        DataModule::class
    ]
)
interface DataComponent : DomainDependencies {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @ApplicationContext context: Context
        ): DataComponent
    }
}

@Module
internal interface DataModule {

    @DataScope
    @Binds
    fun bindPokemonsRepository(impl: PokemonsRepositoryImpl): PokemonsRepository

    @DataScope
    @Binds
    @LocalSource
    fun bindLocalSource(impl: PokemonsLocalDataSource): PokemonsDataSource

    @DataScope
    @Binds
    @NetworkSource
    fun bindNetworkDataSouce(impl: PokemonsNetworkDataSource): PokemonsDataSource

    @DataScope
    @Binds
    fun bindDataSaver(impl: PokemonsLocalDataSource): PokemonsDataSaver

    companion object {
        @DataScope
        @Provides
        fun provideRetrofit(moshi: Moshi): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)
            return Retrofit
                .Builder()
                .baseUrl("https://pokeapi.co/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(FlowCallAdapterFactory())
                .client(httpClient.build())
                .build()
        }

        @DataScope
        @Provides
        fun provideJsonPlaceholderService(retrofit: Retrofit): PokemonsService =
            retrofit.create(PokemonsService::class.java)

        @DataScope
        @Provides
        fun providePokemonsMapper(): PokemonsMapper = PokemonsMapperImpl()

        @DataScope
        @Provides
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences("local_data.prefs", Context.MODE_PRIVATE)

        @DataScope
        @Provides
        fun bindMoshi(): Moshi = Moshi.Builder().build()
    }
}