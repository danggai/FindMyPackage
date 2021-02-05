package com.example.findmypackage.di

import com.example.findmypackage.data.api.ApiInterface
import com.example.findmypackage.data.api.ApiRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val repositoryModule = module {
    single(createdAtStart = false) { ApiRepository(get<Retrofit>().create(ApiInterface::class.java)) }
}