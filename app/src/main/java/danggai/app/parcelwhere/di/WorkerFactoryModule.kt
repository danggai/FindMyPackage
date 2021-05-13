package danggai.app.parcelwhere.di

import androidx.work.WorkerFactory
import danggai.app.parcelwhere.data.api.ApiInterface
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.worker.MyWorkerFactory
import danggai.app.parcelwhere.worker.RefreshWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import retrofit2.Retrofit

val WorkerFactoryModule = module {
    worker { params -> RefreshWorker(get(), get(), workerParams = params.get()) }
}