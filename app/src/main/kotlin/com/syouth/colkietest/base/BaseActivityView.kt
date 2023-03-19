package com.syouth.colkietest.base

import androidx.activity.ComponentActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal abstract class BaseActivityView<V : BaseViewState, I : BaseIntent, T : BaseModel<*, V, I>> : ComponentActivity(), BaseView<V, I, T> {
    private val viewScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    protected abstract val model: T

    protected fun <T> Flow<T>.collectTillHidden(fn: suspend (T) -> Unit = {}): Job =
        onEach(fn).launchIn(viewScope)

    override fun onResume() {
        super.onResume()
        model.onShown()
        model
            .observeViewState()
            .collectTillHidden(this::render)
    }

    override fun onStop() {
        super.onStop()
        model.onHidden()
        viewScope.coroutineContext.cancelChildren()
    }

    protected fun sendIntent(intent: I) = model.processIntent(intent)

    protected abstract fun render(state: V)
}