package com.syouth.colkietest

import android.app.Application
import com.syouth.cplkietest.data.di.DaggerDataComponent
import com.syouth.domain.di.DaggerDomainComponent
import com.syouth.domain.di.DomainComponent

internal class PokemonsApplication : Application() {

    lateinit var domainComponent: DomainComponent

    override fun onCreate() {
        super.onCreate()
        domainComponent = DaggerDomainComponent
            .factory()
            .create(DaggerDataComponent.factory().create(this))
    }
}