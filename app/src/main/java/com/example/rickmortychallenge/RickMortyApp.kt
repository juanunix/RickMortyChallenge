package com.example.rickmortychallenge

import android.app.Application
import com.example.rickmortychallenge.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RickMortyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RickMortyApp)
            modules(appModule)
        }
    }
}
