package com.example.findmypackage.ui.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel(open val app: Application) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    var lvMakeToast: MutableLiveData<String> = MutableLiveData("")
    var lvCopyClipboard: MutableLiveData<String> = MutableLiveData("")

    fun getString(resId: Int): String {
        return app.getString(resId)
    }

    fun Disposable.addCompositeDisposable() {
        compositeDisposable.add(this)
    }



}