package com.example.findmypackage.ui.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel(open val app: Application) : ViewModel() {

    var lvMakeToast: MutableLiveData<String> = MutableLiveData("")

    fun getString(resId: Int): String {
        return app.getString(resId)
    }



}