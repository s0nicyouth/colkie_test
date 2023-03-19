package com.syouth.colkietest.base

internal interface BaseMapper<D : BaseDomainState, V : BaseViewState> {
    operator fun invoke(from: D): V
}