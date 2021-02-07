package com.example.findmypackage.ui.main

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.R
import com.example.findmypackage.core.BaseViewModel
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.res.ResCarrier
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject


class MainViewModel(override val app: Application, private val api: ApiRepository) : BaseViewModel(app) {

    var lvStartAddAct: MutableLiveData<Boolean> = MutableLiveData()
    var lvStartDetailAct: MutableLiveData<Boolean> = MutableLiveData()

    private val rxApiCarrier: PublishSubject<Boolean> = PublishSubject.create()

    private var lvMyTracksList: MutableLiveData<List<ResCarrier>> = MutableLiveData(listOf())
    val _lvMyTracksList = lvMyTracksList

    val compositeDisposable = CompositeDisposable()

    init {

//        compositeDisposable.add( rxApiCarrier
//            .observeOn(Schedulers.newThread())
//            .filter { it }
//            .switchMap {
//                api.carriers()
//            }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe{ res ->
//                //                    for (item in res) {
//                //                        log.e(item.toString())
//                //                    }
//                lvCarrierList.value = res.toList()
//            }
//        )

    }

    fun initUI() {
//        rxApiCarrier.onNext(true)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_add -> {
                lvStartAddAct.value = true
            }
        }
    }

}