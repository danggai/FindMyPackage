package com.example.findmypackage.ui.main

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.R
import com.example.findmypackage.data.AppSession
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.res.ResCarrier
import com.example.findmypackage.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class MainViewModel(override val app: Application, private val api: ApiRepository) : BaseViewModel(app) {

    var lvStartAddAct: MutableLiveData<Boolean> = MutableLiveData()
    var lvStartDetailAct: MutableLiveData<Boolean> = MutableLiveData()

    private val rxApiCarrier: PublishSubject<Boolean> = PublishSubject.create()

    private var lvMyTracksList: MutableLiveData<List<ResCarrier>> = MutableLiveData(listOf())
    val _lvMyTracksList = lvMyTracksList

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
            rxApiCarrier
                .observeOn(Schedulers.newThread())
                .filter { it }
                .switchMap {
                    api.carriers()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ res ->
                    AppSession.setCarrierList(res.toMutableList())
                }
        )
    }

    fun initUI() {
        rxApiCarrier.onNext(true)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_add -> {
                lvStartAddAct.value = true
            }
        }
    }

}