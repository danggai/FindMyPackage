package danggai.app.parcelwhere.ui.track.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.data.local.Tracks
import danggai.app.parcelwhere.data.rxbus.RxBusMainSelectAll
import danggai.app.parcelwhere.ui.base.BaseViewModel
import danggai.app.parcelwhere.util.Event
import danggai.app.parcelwhere.util.NonNullMutableLiveData
import danggai.app.parcelwhere.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class TrackDetailViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao)  : BaseViewModel(app) {

    var lvModifyItemName = MutableLiveData<Event<String>>()
    var lvParcelNotFound = MutableLiveData<Event<String>>()
    var lvGoBack = MutableLiveData<Event<Boolean>>()

    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private val rxDaoUpdate: PublishSubject<TrackEntity> = PublishSubject.create()
    private val rxDaoDelete: PublishSubject<TrackEntity> = PublishSubject.create()
    private val rxDaoUpdateNameById: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private val rxFragmentQuit: PublishSubject<Boolean> = PublishSubject.create()

    var lvTrackEntity: NonNullMutableLiveData<TrackEntity> = NonNullMutableLiveData(TrackEntity("","","","","","","",false))
    var lvTrackData: NonNullMutableLiveData<Tracks> = NonNullMutableLiveData(Tracks("", Tracks.From("",""), Tracks.To("",""), Tracks.State("",""), listOf(), Tracks.Carrier("","","")))

    var lvItemName: NonNullMutableLiveData<String> = NonNullMutableLiveData("")

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
                        lvTrackData.value = res.data
                        rxDaoUpdate.onNext(
                            TrackEntity(lvTrackEntity.value.trackId, "", res.data.from.name, res.data.carrier.id, res.data.carrier.name, res.data.progresses[res.data.progresses.size-1].time, res.data.state.text, false)
                        )
                    }
                    Constant.META_CODE_BAD_REQUEST,
                    Constant.META_CODE_NOT_FOUND -> {
                        log.e()
                        val msg = String.format(getString(R.string.error), res.meta.code, getString(R.string.msg_percel_not_exist_error))
                        lvParcelNotFound.value = Event( String.format(getString(R.string.dialog_forcely_delete_parcel), msg, getString(R.string.error_reason_404)) )
                    }
                    Constant.META_CODE_SERVER_ERROR-> {
                        log.e()
                        val msg = String.format(getString(R.string.error), res.meta.code, getString(R.string.msg_carrier_network_error))
                        lvParcelNotFound.value = Event( String.format(getString(R.string.dialog_forcely_delete_parcel), msg, getString(R.string.error_reason_500)) )
                    }
                    else -> {
                        log.e()
                        lvMakeToast.value = Event( String.format(getString(R.string.error), res.meta.code, res.meta.message) )
                        goBack()
                    }
                }
            }, {
                it.message?.let { msg ->
                    log.e(msg)
                    lvMakeToast.value = Event( String.format(getString(R.string.error), 0, msg) )
                    goBack()
                }
            }).addCompositeDisposable()

        rxDaoUpdate
            .observeOn(Schedulers.newThread())
            .map { item ->
                log.e(item)
                TrackEntity(item.trackId, dao.selectItemNameById(item.trackId), item.fromName, item.carrierId, item.carrierName, item.recentTime, item.recentStatus, item.isRefreshed)
            }
            .map { item ->
                dao.update(item)
                item
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ item ->
                log.e(item)
                if (lvTrackEntity.value.isRefreshed) {
                    RxBusMainSelectAll.getSubject()?.onNext(true)
                }
                lvTrackEntity.value = item
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxDaoDelete
            .observeOn(Schedulers.newThread())
            .subscribe({ item ->
                log.e()
                dao.deleteById(item.trackId)
                RxBusMainSelectAll.getSubject()?.onNext(true)

                rxFragmentQuit.onNext(true)
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxDaoUpdateNameById
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                setItemName(it.first)
                true
            }
            .observeOn(Schedulers.newThread())
            .subscribe ({ item ->
                log.e(item)
                dao.updateNameById(name = item.first, id = item.second)
                RxBusMainSelectAll.getSubject()?.onNext(true)
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxFragmentQuit
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                log.e()
                lvMakeToast.value = Event(getString(R.string.toast_parcel_delete_done))
                lvGoBack.value = Event(true)
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()
    }

    fun initUi(item: TrackEntity) {
        log.e()
        lvTrackEntity.value = item
        setItemName(item.itemName)
        rxApiCarrierTracks.onNext(Pair(item.carrierId, item.trackId))
    }

    fun onClickItemName() {
        log.e()
        lvModifyItemName.value = Event(
            if (lvItemName.value == getString(R.string.item_name_empty)) "" else lvItemName.value
        )
    }

    fun setItemName(name: String) {
        log.e()
        lvItemName.value = if (name.isNotEmpty()) {
            log.e()
            name
        } else {
            log.e()
            getString(R.string.item_name_empty)
        }
    }

    fun updateItemName(name: String) {
        log.e()
        rxDaoUpdateNameById.onNext(Pair(name.trim(), lvTrackEntity.value.trackId))
    }

    fun goBack() {
        log.e()
        lvGoBack.value = Event(true)
    }

    fun deleteItem() {
        log.e()
        rxDaoDelete.onNext(lvTrackEntity.value)
    }

}