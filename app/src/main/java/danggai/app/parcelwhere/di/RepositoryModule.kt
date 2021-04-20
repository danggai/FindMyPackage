package danggai.app.parcelwhere.di

import danggai.app.parcelwhere.data.api.ApiInterface
import danggai.app.parcelwhere.data.api.ApiRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val repositoryModule = module {
    single(createdAtStart = false) { ApiRepository(get<Retrofit>().create(ApiInterface::class.java)) }
}