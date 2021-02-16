package com.example.findmypackage.ui.main

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.R
import com.example.findmypackage.data.AppSession
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.db.track.TrackDao
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.ui.base.BaseViewModel
import com.example.findmypackage.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class MainViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartAddAct: MutableLiveData<Boolean> = MutableLiveData()
    var lvStartDetailAct: MutableLiveData<TrackEntity> = MutableLiveData()

    private val rxApiCarrier: PublishSubject<Boolean> = PublishSubject.create()
    private val rxDaoSelectAll: PublishSubject<Boolean> = PublishSubject.create()

    private var _lvMyTracksList: MutableLiveData<List<TrackEntity>> = MutableLiveData(listOf())
    val lvMyTracksList = _lvMyTracksList

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable
            .addAll(
                rxApiCarrier
                    .observeOn(Schedulers.newThread())
                    .filter { it }
                    .switchMap {
                        api.carriers()
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({ res ->
                        AppSession.setCarrierList(res)
                    }, {
                        it.message?.let { msg -> log.e(msg) }
                    })
                , rxDaoSelectAll
                    .observeOn(Schedulers.newThread())
                    .filter { it }
                    .switchMap {
                        dao.selectAll()
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({ items ->
                        _lvMyTracksList.value = items
                        log.e(items)
                    }, {
                        it.message?.let { msg -> log.e(msg) }
                    })
        )
    }

    fun getAllTrackList() {
        rxDaoSelectAll.onNext(true)
    }

    fun initUI() {
        getAllTrackList()
        rxApiCarrier.onNext(true)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_add -> {
                lvStartAddAct.value = true
            }
        }
    }

    fun onClickItem(item: TrackEntity) {
        lvStartDetailAct.value = item
    }

}