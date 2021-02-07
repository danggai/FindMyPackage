package com.example.findmypackage.ui.trackAdd

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.R
import com.example.findmypackage.core.BaseViewModel
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.res.ResCarrier
import com.example.findmypackage.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class TrackAddViewModel(override val app: Application, private val api: ApiRepository) : BaseViewModel(app) {

    val lvPostNum: MutableLiveData<String> = MutableLiveData("")
    val lvCarrier: MutableLiveData<String> = MutableLiveData("")

    private var lvCarrierList: MutableLiveData<MutableList<ResCarrier>> = MutableLiveData(mutableListOf())
    val _lvCarrierList = lvCarrierList
    var lvClearItem: MutableLiveData<Boolean> = MutableLiveData(false)

    private val rxApiCarrier: PublishSubject<Boolean> = PublishSubject.create()

    val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add( rxApiCarrier
            .observeOn(Schedulers.newThread())
            .filter { it }
            .switchMap {
                api.carriers()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ res ->
                lvCarrierList.value = res.toMutableList()
            }
        )
    }

    fun initUI() {
        rxApiCarrier.onNext(true)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_search -> {
                log.d()
            }
            else -> {

            }
        }
    }

}