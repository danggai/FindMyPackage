package danggai.app.parcelwhere

import android.app.Application
import danggai.app.parcelwhere.di.*
import danggai.app.parcelwhere.util.log
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import java.io.IOException
import java.net.SocketException


class App: Application() {

    @KoinExperimentalAPI
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            workManagerFactory()
            modules(listOf(ViewModelModule, NetworkModule, repositoryModule, RoomModule, WorkerModule))
        }

        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                log.e("undeliverable exception: ${e.cause?.message?:"unknown"}")
            }
            if (e is IOException || e is SocketException || e is InterruptedException) {
                return@setErrorHandler
            }
            if (e is NullPointerException || e is IllegalArgumentException || e is IllegalStateException) {
                Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(Thread.currentThread(), e)
                return@setErrorHandler
            }
        }
    }
}