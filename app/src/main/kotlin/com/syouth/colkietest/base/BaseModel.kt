package com.syouth.colkietest.base

import kotlinx.coroutines.flow.Flow

internal interface BaseModel<D : BaseDomainState, V : BaseViewState, I : BaseIntent> {
    fun onShown()
    fun onHidden()
    fun processIntent(intent: I)
    fun observeViewState(): Flow<V>
}