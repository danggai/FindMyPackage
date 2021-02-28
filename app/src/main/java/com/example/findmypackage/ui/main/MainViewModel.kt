package com.example.findmypackage.ui.main

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.Constant
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

    var lvStartAddAct: MutableLiveData<Boolean> = MutableLiveData(false)
    var lvStartDetailAct: MutableLiveData<TrackEntity> = MutableLiveData()
    var lvIsRefresh: MutableLiveData<Boolean> = MutableLiveData(false)

    private val rxApiCarrier: PublishSubject<Boolean> = PublishSubject.create()
    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private val rxDaoSelectAll: PublishSubject<Boolean> = PublishSubject.create()
    private val rxDaoUpdate: PublishSubject<TrackEntity> = PublishSubject.create()

    private var _lvMyTracksList: MutableLiveData<List<TrackEntity>> = MutableLiveData(listOf())
    val lvMyTracksList = _lvMyTracksList

    private var lvRefreshStack: MutableLiveData<MutableList<Boolean>> = MutableLiveData(mutableListOf())

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
                , rxDaoUpdate
                    .observeOn(Schedulers.newThread())
                    .subscribe ({ item ->
                        dao.update(item)
                    }, {
                        it.message?.let { msg -> log.e(msg) }
                    })
                , rxApiCarrierTracks
                    .observeOn(Schedulers.newThread())
                    .switchMap {
                        api.carriersTracks(it.first, it.second)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ res ->
                        when (res.meta.code) {
                            Constant.META_CODE_SUCCESS -> {
                                log.e(res.data)
                                rxDaoUpdate.onNext(
                                    TrackEntity(res.trackId, "", res.data.from.name, res.data.carrier.id, res.data.carrier.name, res.data.progresses[res.data.progresses.size-1].time, res.data.state.text)
                                )
                                checkRefreshing()
                            }
                            Constant.META_CODE_BAD_REQUEST,
                            Constant.META_CODE_NOT_FOUND,
                            Constant.META_CODE_SERVER_ERROR -> {
                                lvMakeToast.value = getString(R.string.msg_network_error)
                                checkRefreshing()
                            }
                            else -> {
                                checkRefreshing()
                            }
                        }
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

    fun refreshAll() {
        for (item in _lvMyTracksList.value!!) {
            log.e(item)
            if (item.recentStatus != Constant.STATE_DELIVERY_COMPLETE) {
                lvRefreshStack.value?.add(true)
                rxApiCarrierTracks.onNext(Pair(item.carrierId, item.trackId))
            }
        }
    }

    private fun checkRefreshing() {
        lvRefreshStack.value?.remove(true)
        if (lvRefreshStack.value?.size == 0) lvIsRefresh.value = false
    }

}