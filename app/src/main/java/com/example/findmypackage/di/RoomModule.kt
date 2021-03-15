package com.example.findmypackage.di

import com.example.findmypackage.data.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * @author Leopold
 */
val RoomModule = module {
    single { AppDatabase.getInstance(androidApplication()) }
    single(createdAtStart = true) { get<AppDatabase>().trackDao() }
}