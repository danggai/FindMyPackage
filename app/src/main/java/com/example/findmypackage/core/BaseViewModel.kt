package com.example.findmypackage.core

import android.app.Application
import androidx.lifecycle.ViewModel

open class BaseViewModel(open val app: Application) : ViewModel() {

    fun getString(resId: Int): String {
        return app.getString(resId)
    }

}