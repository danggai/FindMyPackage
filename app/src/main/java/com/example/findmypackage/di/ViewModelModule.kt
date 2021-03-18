package com.example.findmypackage.di

import com.example.findmypackage.ui.track.add.TrackAddViewModel
import com.example.findmypackage.ui.main.MainViewModel
import com.example.findmypackage.ui.setting.SettingViewModel
import com.example.findmypackage.ui.track.detail.TrackDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { SettingViewModel(get(), get(), get()) }
    viewModel { TrackAddViewModel(get(), get(), get()) }
    viewModel { TrackDetailViewModel(get(), get(), get()) }
}