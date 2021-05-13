package danggai.app.parcelwhere.di

import danggai.app.parcelwhere.worker.RefreshWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val WorkerFactoryModule = module {
    worker { params -> RefreshWorker(get(), workerParams = params.get(), get(), get()) }
}