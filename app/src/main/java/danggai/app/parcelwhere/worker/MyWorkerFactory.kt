package danggai.app.parcelwhere.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.util.log

class MyWorkerFactory(private val api: ApiRepository): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        log.e(workerClassName)
        log.e(RefreshWorker::class.java.name)
        return when (workerClassName) {
            RefreshWorker::class.java.name -> {
                RefreshWorker(api, appContext, workerParameters)
            }
            else -> null        // If you return null, delegate base class to the default workerFactory.
        }
    }

}