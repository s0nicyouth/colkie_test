package com.syouth.colkietest.pokemons_list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.syouth.colkietest.PokemonsApplication
import com.syouth.colkietest.base.BaseActivityView
import com.syouth.colkietest.pokemon_details.PokemonDetailsContract
import com.syouth.colkietest.pokemon_details.PokemonDetailsView
import com.syouth.colkietest.pokemons_list.di.DaggerPokemonsListComponent
import com.syouth.colkietest.ui.theme.ColkieTestTheme
import com.syouth.domain.entities.PokemonInfo

internal class PokemonsListView : BaseActivityView<PokemonsListContract.ViewState, PokemonsListContract.Intent, PokemonsListContract.Model>(), PokemonsListContract.View {

    private val stateVal = mutableStateOf<PokemonsListContract.ViewState>(PokemonsListContract.ViewState.Loading)
    override val model: PokemonsListContract.Model by lazy(LazyThreadSafetyMode.NONE) {
        DaggerPokemonsListComponent
            .factory()
            .create((application as PokemonsApplication).domainComponent)
            .model
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColkieTestTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    State(stateVal)
                }
            }
        }
    }

    override fun render(state: PokemonsListContract.ViewState) {
        stateVal.value = state
    }

    @Composable
    fun State(state: State<PokemonsListContract.ViewState>) {
        val s = remember { state }
        Column {
            TopAppBar(
                elevation = 4.dp,
                title = { Text("Colkie") },
                navigationIcon = {
                    IconButton(onClick = { finish() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                }, actions = {
                    IconButton(onClick = { sendIntent(PokemonsListContract.Intent.Refresh) }) {
                        Icon(Icons.Filled.Refresh, null)
                    }
                })
            when (val value = s.value) {
                PokemonsListContract.ViewState.ErrorState -> Error()
                PokemonsListContract.ViewState.Loading -> Loading()
                is PokemonsListContract.ViewState.PokemonsViewState -> PokemonsList(pokemons = value.pokemons, lastPage = value.lastPage, hasError = value.hasError)
            }
        }
    }

    @Composable
    fun Loading() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
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

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun PokemonsList(pokemons: List<PokemonInfo>, lastPage: Int, hasError: Boolean) {
        if (hasError) {
            Toast.makeText(this, "Error occurred during loading! Check internet connection.", Toast.LENGTH_LONG).show()
        }
        val scrollState = rememberLazyListState()

        LazyColumn(state = scrollState) {
            itemsIndexed(items = pokemons, key = { _, item -> item.id }) { index, item ->
                Row(
                    modifier = Modifier.clickable {
                        startActivity(Intent(this@PokemonsListView, PokemonDetailsView::class.java).apply { putExtra(PokemonDetailsContract.POKEMON_ID, item.id) })
                    }
                ) {
                    Column(Modifier.padding(10.dp)) {
                        Text("id = ${item.id}")
                        Text("name = ${item.name}")
                    }
                    GlideImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(alignment = Alignment.CenterVertically)
                            .size(50.dp, 50.dp),
                        model = item.sprites.front,
                        contentDescription = item.name
                    )
                }
                if (index < pokemons.lastIndex) {
                    Divider(color = Color.Cyan, thickness = 1.dp)
                }
            }
            // TODO: this probably can be done in a more efficient way.
            scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.let {
                if (it.index == pokemons.lastIndex) {
                    sendIntent(PokemonsListContract.Intent.NextPage(lastPage + 1))
                }
            } ?: run { sendIntent(PokemonsListContract.Intent.NextPage(lastPage + 1)) }
        }
    }
}