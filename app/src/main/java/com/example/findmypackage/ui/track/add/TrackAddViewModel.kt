package com.example.findmypackage.ui.track.add

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.Constant
import com.example.findmypackage.R
import com.example.findmypackage.ui.base.BaseViewModel
import com.example.findmypackage.data.AppSession
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.db.track.TrackDao
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.util.Event
import com.example.findmypackage.util.NonNullMutableLiveData
import com.example.findmypackage.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class TrackAddViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartDetailAct = MutableLiveData<Event<Boolean>>()

    var lvCarrierId: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    var lvTrackId: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    var lvItemName: NonNullMutableLiveData<String> = NonNullMutableLiveData("")

    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private val rxDaoInsert: PublishSubject<TrackEntity> = PublishSubject.create()

    private var _lvCarrierList: NonNullMutableLiveData<List<Carrier>> = NonNullMutableLiveData(listOf())
    val lvCarrierList = _lvCarrierList


    init {
        rxApiCarrierTracks
            .observeOn(Schedulers.newThread())
            .switchMap {
                log.e()
                api.carriersTracks(it.first, it.second)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                log.e(res)
                when (res.meta.code) {
                    Constant.META_CODE_SUCCESS -> {
                        lvStartDetailAct.value = Event(true)
                        rxDaoInsert.onNext(
                            TrackEntity(lvTrackId.value, lvItemName.value,  res.data.from.name,  res.data.carrier.id, res.data.carrier.name, res.data.progresses[res.data.progresses.size-1].time, res.data.state.text)
                        )
                    }
                    Constant.META_CODE_BAD_REQUEST,
                    Constant.META_CODE_NOT_FOUND,
                    Constant.META_CODE_SERVER_ERROR -> {
                        lvMakeToast.value = getString(R.string.msg_network_error)
                    }
                    else -> {

                    }
                }
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxDaoInsert
            .observeOn(Schedulers.newThread())
            .subscribe ({ item ->
                dao.insertWithReplace(item)
                log.e(item)
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        _lvCarrierList.value = AppSession.getCarrierList()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_search -> {
                log.d()
                trackSearch()
            }
            else -> {
                log.e()
            }
        }
    }

    fun onClickItem(view: Carrier) {
        log.e(view.id)
        lvCarrierId.value = if (view.idx == -1) {
            ""
        } else {
            view.id
        }
    }

    private fun trackSearch() {
        when {
            lvTrackId.value.isNotEmpty() && lvCarrierId.value.isNotEmpty() -> {
                rxApiCarrierTracks.onNext(Pair(lvCarrierId.value, lvTrackId.value))
            } lvCarrierId.value.isEmpty() -> {
                lvMakeToast.value = getString(R.string.msg_carrier_empty)
            } lvTrackId.value.isEmpty() -> {
                lvMakeToast.value = getString(R.string.msg_track_id_empty)
            } else -> {
                log.e()
            }
        }
    }

}