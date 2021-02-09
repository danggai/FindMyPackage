package com.example.findmypackage.di

import com.example.findmypackage.data.db.Database
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val RoomModule = module {
    single {
        Database(androidApplication().applicationContext).invoke()
    }
}