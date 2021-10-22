package danggai.app.parcelwhere.ui.track.add

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.AppSession
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.data.local.Carrier
import danggai.app.parcelwhere.ui.base.BaseViewModel
import danggai.app.parcelwhere.util.CarrierUtil
import danggai.app.parcelwhere.util.Event
import danggai.app.parcelwhere.util.NonNullMutableLiveData
import danggai.app.parcelwhere.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class TrackAddViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartDetailAct = MutableLiveData<Event<Boolean>>()
    var lvGoBack = MutableLiveData<Event<Boolean>>()
    var lvProgressVisibility = MutableLiveData<Event<Boolean>>()
    var lvDialogAddItemForcibly = MutableLiveData<Event<String>>()

    var lvItemSetChanged: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)

    var lvCarrierId: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    var lvTrackId: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    var lvItemName: NonNullMutableLiveData<String> = NonNullMutableLiveData("")

    private val rxApiCarrier: PublishSubject<Boolean> = PublishSubject.create()
    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private val rxDaoInsert: PublishSubject<TrackEntity> = PublishSubject.create()

    private var _lvCarrierList: NonNullMutableLiveData<List<Carrier>> = NonNullMutableLiveData(listOf())
    val lvCarrierList = _lvCarrierList

    init {
        initRx()
        _lvCarrierList.value = AppSession.getCarrierList()
    }

    fun initUi() {
        log.e()
        if (AppSession.getCarrierList().size < 2) rxApiCarrier.onNext(true)
    }

    private fun initRx() {
        rxApiCarrier
            .observeOn(Schedulers.newThread())
            .filter { it }
            .switchMap {
                api.carriers()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ res ->
                AppSession.setCarrierList(res)
                _lvCarrierList.value = res.data
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxApiCarrierTracks
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                lvProgressVisibility.value = Event(true)
                it
            }
            .observeOn(Schedulers.newThread())
            .switchMap {
                log.e()
                api.carriersTracks(it.first, it.second)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                log.e(res)
                lvProgressVisibility.value = Event(false)
                when (res.meta.code) {
                    Constant.META_CODE_SUCCESS -> {
                        log.e()
                        lvStartDetailAct.value = Event(true)
                        rxDaoInsert.onNext(
                            if (res.data.progresses.isEmpty()) {
                                TrackEntity(lvTrackId.value, lvItemName.value,  res.data.from.name,  res.data.carrier.id, res.data.carrier.name, "", res.data.state.text, "", false)
                            } else {
                                TrackEntity(lvTrackId.value, lvItemName.value,  res.data.from.name,  res.data.carrier.id, res.data.carrier.name, res.data.progresses[res.data.progresses.size-1].time, res.data.state.text, res.data.progresses[res.data.progresses.size-1].location.name, false)
                            }
                        )
                    }
                    Constant.META_CODE_BAD_REQUEST,
                    Constant.META_CODE_NOT_FOUND -> {
                        log.e()
                        val msg = String.format(getString(R.string.error), res.meta.code, getString(R.string.msg_percel_not_exist_error))
                        lvDialogAddItemForcibly.value = Event( String.format(getString(R.string.dialog_forcibly_add_parcel), msg, getString(R.string.error_reason_404)) )
                    }
                    Constant.META_CODE_SERVER_ERROR -> {
                        log.e()
                        val msg = String.format(getString(R.string.error), res.meta.code, getString(R.string.msg_carrier_network_error))
                        lvDialogAddItemForcibly.value = Event( String.format(getString(R.string.dialog_forcibly_add_parcel), msg, getString(R.string.error_reason_500)) )
                    }
                    else -> {
                        log.e()
                        lvMakeToast.value = Event( String.format(getString(R.string.error), res.meta.code, res.meta.message) )
                    }
                }
            }, {
                it.message?.let { msg ->
                    log.e(msg)
                    lvProgressVisibility.value = Event(false)
                    lvMakeToast.value = Event( String.format(getString(R.string.error), 0, msg) )
                }
                initRx()
            }).addCompositeDisposable()

        rxDaoInsert
            .observeOn(Schedulers.newThread())
            .map { item ->
                dao.insertWithReplace(item)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ item ->
                log.e(item)
                lvGoBack.value = Event(true)
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()
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

    fun onClickItem(item: Carrier) {
        val postCarrierId = lvCarrierId.value
        val selectCarrierId = item.id
        lvCarrierId.value = if (selectCarrierId == postCarrierId) {
            ""
        } else {
            selectCarrierId
        }

        lvItemSetChanged.value = true
    }

    private fun trackSearch() {
        when {
            lvTrackId.value.isNotEmpty() && lvCarrierId.value.isNotEmpty() -> {
                rxApiCarrierTracks.onNext(Pair(lvCarrierId.value, lvTrackId.value))
            } lvCarrierId.value.isEmpty() -> {
                lvMakeToast.value = Event(getString(R.string.msg_carrier_empty))
            } lvTrackId.value.isEmpty() -> {
                lvMakeToast.value = Event(getString(R.string.msg_track_id_empty))
            } else -> {
                log.e()
            }
        }
    }

    fun forciblyAddItem() {
        log.e()
        rxDaoInsert.onNext(
            TrackEntity(lvTrackId.value, lvItemName.value, "", lvCarrierId.value, CarrierUtil.getCarrierName(lvCarrierId.value), "", "", "", true)
        )
    }

}