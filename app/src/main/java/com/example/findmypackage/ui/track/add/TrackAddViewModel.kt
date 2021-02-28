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
import com.example.findmypackage.util.CommonFuntion
import com.example.findmypackage.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.text.SimpleDateFormat
import java.util.*


class TrackAddViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartDetailAct: MutableLiveData<Boolean> = MutableLiveData(false)

    var lvCarrierId: MutableLiveData<String> = MutableLiveData("")
    var lvTrackId: MutableLiveData<String> = MutableLiveData("")
    var lvItemName: MutableLiveData<String> = MutableLiveData("")

    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private val rxDaoInsert: PublishSubject<TrackEntity> = PublishSubject.create()

    private var _lvCarrierList: MutableLiveData<List<Carrier>> = MutableLiveData(listOf())
    val lvCarrierList = _lvCarrierList

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.addAll(
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
                            lvStartDetailAct.value = true
                            rxDaoInsert.onNext(TrackEntity(lvTrackId.value?:"0", lvItemName.value?:getString(R.string.item_name_empty),  res.data.from.name,  res.data.carrier.id, res.data.carrier.name, CommonFuntion.now(), res.data.state.text))
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
                })
            , rxDaoInsert
                .observeOn(Schedulers.newThread())
                .subscribe ({ item ->
                    dao.insert(item)
                    log.e(item)
                }, {
                    it.message?.let { msg -> log.e(msg) }
                })
        )

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
            lvTrackId.value?.isNotEmpty()?:false && lvCarrierId.value?.isNotEmpty()?:false -> {
                rxApiCarrierTracks.onNext(Pair(lvCarrierId.value!!, lvTrackId.value!!))
            } lvCarrierId.value?.isEmpty() == true -> {
                lvMakeToast.value = getString(R.string.msg_carrier_empty)
            } lvTrackId.value?.isEmpty() == true -> {
                lvMakeToast.value = getString(R.string.msg_track_id_empty)
            } else -> {
                log.e()
            }
        }
    }

}