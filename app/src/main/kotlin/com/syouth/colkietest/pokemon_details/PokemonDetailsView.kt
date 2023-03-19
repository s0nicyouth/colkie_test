package com.syouth.colkietest.pokemon_details

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.syouth.colkietest.PokemonsApplication
import com.syouth.colkietest.base.BaseActivityView
import com.syouth.colkietest.pokemon_details.di.DaggerPokemonDetailsComponent
import com.syouth.colkietest.ui.theme.ColkieTestTheme

internal class PokemonDetailsView : BaseActivityView<PokemonDetailsContract.ViewState, PokemonDetailsContract.Intent, PokemonDetailsContract.Model>(), PokemonDetailsContract.View {

    private val stateValue = mutableStateOf<PokemonDetailsContract.ViewState>(PokemonDetailsContract.ViewState.Loading)
    override val model: PokemonDetailsContract.Model by lazy(LazyThreadSafetyMode.NONE) {
        DaggerPokemonDetailsComponent
            .factory()
            .create(
                intent.getIntExtra(PokemonDetailsContract.POKEMON_ID, -1),
                (application as PokemonsApplication).domainComponent
            )
            .model
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColkieTestTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    State(stateValue)
                }
            }
        }
    }

    override fun render(state: PokemonDetailsContract.ViewState) {
        stateValue.value = state
    }

    @Composable
    fun State(state: State<PokemonDetailsContract.ViewState>) {
        val rememberedState = remember { state }
        Column {
            TopAppBar(
                elevation = 4.dp,
                title = { Text("Colkie") },
                navigationIcon = {
                    IconButton(onClick = { finish() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                }, actions = {
                    IconButton(onClick = { sendIntent(PokemonDetailsContract.Intent.Refresh) }) {
                        Icon(Icons.Filled.Refresh, null)
                    }
                })
            when (val value = rememberedState.value) {
                PokemonDetailsContract.ViewState.Error -> Error()
                PokemonDetailsContract.ViewState.Loading -> Loading()
                is PokemonDetailsContract.ViewState.PokemonInfoViewState -> PokemonDetails(state = value)
            }
        }
    }

    @Composable
    fun Error() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Error occured",
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun Loading() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun PokemonDetails(state: PokemonDetailsContract.ViewState.PokemonInfoViewState) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(model = state.pokemonInfo.sprites.front, contentDescription = state.pokemonInfo.name, modifier = Modifier.size(200.dp, 200.dp))
            Text("name: ${state.pokemonInfo.name}")
            Text("weight: ${state.pokemonInfo.weight}")
            Text("height: ${state.pokemonInfo.height}")
        }
    }
}