package com.example.rickmortychallenge.di

import com.example.rickmortychallenge.data.network.CharacterApiService
import com.example.rickmortychallenge.data.repository.NetworkCharacterRepository
import com.example.rickmortychallenge.domain.repository.CharacterRepository
import com.example.rickmortychallenge.ui.viewmodel.CharacterViewModel
import com.example.rickmortychallenge.ui.detail.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://rickandmortyapi.com/api/"

val appModule = module {
    // Retrofit network dependency
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // CharacterApiService dependency
    single<CharacterApiService> {
        get<Retrofit>().create(CharacterApiService::class.java)
    }

    // CharacterRepository provider via singleOf constructor reference mapped to domain contract
    singleOf(::NetworkCharacterRepository) { bind<CharacterRepository>() }

    // Presentation ViewModels via viewModelOf constructor reference
    viewModelOf(::CharacterViewModel)
    viewModelOf(::DetailViewModel)
}
