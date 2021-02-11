package com.example.findmypackage

import android.app.Application
import com.example.findmypackage.di.NetworkModule
import com.example.findmypackage.di.RoomModule
import com.example.findmypackage.di.ViewModelModule
import com.example.findmypackage.di.repositoryModule
import com.example.findmypackage.util.log
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.io.IOException
import java.net.SocketException

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(ViewModelModule, NetworkModule, repositoryModule, RoomModule))
        }

        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                log.e("undeliverable exception: ${e.cause?.message?:"unknown"}")
            }
            if (e is IOException || e is SocketException || e is InterruptedException) {
                return@setErrorHandler
            }
            if (e is NullPointerException || e is IllegalArgumentException || e is IllegalStateException) {
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), e)
                return@setErrorHandler
            }
        }
    }

}