package com.syouth.colkietest.base

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal abstract class BaseModelImpl<D : BaseDomainState, V : BaseViewState, I : BaseIntent>(
    private val stateMapper: BaseMapper<D, V>
) : BaseModel<D, V, I> {

    protected abstract val initialState: D
    protected var currentState: D = initialState

    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val stateFlow: MutableStateFlow<V> = MutableStateFlow(stateMapper(currentState))

    protected fun <T> Flow<T>.collectTillHidden(fn: suspend (T) -> Unit = {}): Job =
        onEach(fn).launchIn(modelScope)

    override fun onShown() {}

    override fun onHidden() {
        modelScope.coroutineContext.cancelChildren()
    }

    override fun observeViewState(): Flow<V> = stateFlow.asStateFlow()

    protected fun updateState(fn: (s: D) -> D) {
        currentState = fn(currentState)
        stateFlow.tryEmit(stateMapper(currentState))
    }
}