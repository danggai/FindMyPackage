package danggai.app.parcelwhere.ui.track.add

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.ui.base.BaseViewModel
import danggai.app.parcelwhere.data.AppSession
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.data.local.Carrier
import danggai.app.parcelwhere.util.Event
import danggai.app.parcelwhere.util.NonNullMutableLiveData
import danggai.app.parcelwhere.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class TrackAddViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartDetailAct = MutableLiveData<Event<Boolean>>()

    var lvCarrierId: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    var lvTrackId: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    var lvItemName: NonNullMutableLiveData<String> = NonNullMutableLiveData("")

    private val rxApiCarrier: PublishSubject<Boolean> = PublishSubject.create()
    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private val rxDaoInsert: PublishSubject<TrackEntity> = PublishSubject.create()

    private var _lvCarrierList: NonNullMutableLiveData<List<Carrier>> = NonNullMutableLiveData(listOf())
    val lvCarrierList = _lvCarrierList

    init {
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
                        lvMakeToast.value = Event(getString(R.string.msg_not_exist_network_error))
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

    fun initUi() {
        log.e()
        if (AppSession.getCarrierList().size < 2) rxApiCarrier.onNext(true)
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
                lvMakeToast.value = Event(getString(R.string.msg_carrier_empty))
            } lvTrackId.value.isEmpty() -> {
                lvMakeToast.value = Event(getString(R.string.msg_track_id_empty))
            } else -> {
                log.e()
            }
        }
    }

}