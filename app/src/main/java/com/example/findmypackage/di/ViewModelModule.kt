package com.example.findmypackage.di

import com.example.findmypackage.ui.trackAdd.TrackAddViewModel
import com.example.findmypackage.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { TrackAddViewModel(get(), get()) }
}