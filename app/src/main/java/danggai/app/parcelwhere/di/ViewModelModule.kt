package danggai.app.parcelwhere.di

import danggai.app.parcelwhere.ui.track.add.TrackAddViewModel
import danggai.app.parcelwhere.ui.main.MainViewModel
import danggai.app.parcelwhere.ui.setting.SettingViewModel
import danggai.app.parcelwhere.ui.track.detail.TrackDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { SettingViewModel(get(), get(), get()) }
    viewModel { TrackAddViewModel(get(), get(), get()) }
    viewModel { TrackDetailViewModel(get(), get(), get()) }
}