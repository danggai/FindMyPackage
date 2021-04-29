package danggai.app.parcelwhere.ui.main

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.AppSession
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.data.local.TrackListItem
import danggai.app.parcelwhere.ui.base.BaseViewModel
import danggai.app.parcelwhere.util.Event
import danggai.app.parcelwhere.util.NonNullMutableLiveData
import danggai.app.parcelwhere.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*


class MainViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartAddAct = MutableLiveData<Event<Boolean>>()
    var lvStartSettingAct = MutableLiveData<Event<Boolean>>()
    var lvStartDetailAct = MutableLiveData<Event<TrackEntity>>()
    var lvCopyClipboard = MutableLiveData<Event<String>>()
    var lvIsFirstInit: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(true)
    var lvIsRefreshing: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)

    private val rxApiCarrier: PublishSubject<Boolean> = PublishSubject.create()
    private val rxApiCarrierTracks: PublishSubject<TrackListItem> = PublishSubject.create()
    private val rxDaoSelectAll: PublishSubject<Boolean> = PublishSubject.create()
    private val rxDaoUpdate: PublishSubject<TrackEntity> = PublishSubject.create()
    private val rxDaoDelete: PublishSubject<TrackEntity> = PublishSubject.create()

    private var _lvMyTracksList: NonNullMutableLiveData<List<TrackListItem>> = NonNullMutableLiveData(listOf())
    val lvMyTracksList = _lvMyTracksList

    private var lvRefreshSwitch: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)

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
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxDaoSelectAll
            .observeOn(Schedulers.newThread())
            .filter { it }
            .switchMap {
                log.e()
                dao.selectAll()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ items ->
                log.e(items)
                val trackItems = mutableListOf<TrackListItem>()
                for (item in items) {
                    trackItems.add(TrackListItem(item, NonNullMutableLiveData(false),  false))
                }
                _lvMyTracksList.value = trackItems

                if (lvRefreshSwitch.value) {
                    lvRefreshSwitch.value = false
                    refreshAll()
                }

                rxDaoSelectAll.onComplete()
            }, {
                it.message?.let { msg -> log.e(msg) }
            }, {
                log.e()
            }).addCompositeDisposable()

        rxDaoUpdate
            .observeOn(Schedulers.newThread())
            .map { item ->
                TrackEntity(item.trackId, dao.selectItemNameById(item.trackId), item.fromName, item.carrierId, item.carrierName, item.recentTime, item.recentStatus)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map { trackEntity ->
                _lvMyTracksList.value[getIndexById(trackEntity.trackId)].let {
                    it.trackEntity = trackEntity
                    it.isRefreshing.value = false
                    checkRefreshing()
                }

                trackEntity
            }
            .observeOn(Schedulers.newThread())
            .subscribe ({ trackEntity ->
                log.e("refreshed item.trackId -> ${trackEntity.trackId}")

                dao.update(trackEntity)
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxDaoDelete
            .observeOn(Schedulers.newThread())
            .subscribe({ item ->
                dao.deleteById(item.trackId)
                rxDaoSelectAll.onNext(true)
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxApiCarrierTracks
            .observeOn(Schedulers.newThread())
            .switchMap {
                api.carriersTracks(it.trackEntity.carrierId, it.trackEntity.trackId)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                when (res.meta.code) {
                    Constant.META_CODE_SUCCESS -> {
                        log.e(res.data)
                        rxDaoUpdate.onNext(
                            TrackEntity(res.data.trackId, "", res.data.from.name, res.data.carrier.id, res.data.carrier.name, res.data.progresses[res.data.progresses.size-1].time, res.data.state.text)
                        )
                    }
                    Constant.META_CODE_BAD_REQUEST,
                    Constant.META_CODE_NOT_FOUND,
                    Constant.META_CODE_SERVER_ERROR -> {
                        log.e()
                        checkRefreshing()
                    }
                    else -> {
                        log.e()
                        checkRefreshing()
                    }
                }
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()
    }

    fun initUI() {
        if (!lvIsFirstInit.value) return else lvIsFirstInit.value = false
        log.e()
        lvRefreshSwitch.value = true
        getAllTrackList()
        rxApiCarrier.onNext(true)
    }

    private fun isDeliveryCompleted(trackEntity: TrackEntity): Boolean {
//        return false
        return trackEntity.recentStatus!!.contains(Constant.STATE_DELIVERY_COMPLETE)
    }

    fun refreshAll() {
        log.e()

        for (idx in _lvMyTracksList.value.indices) {
            if (!isDeliveryCompleted(_lvMyTracksList.value[idx].trackEntity)) {
                log.e()
                _lvMyTracksList.value[idx].isRefreshing.value = true

                rxApiCarrierTracks.onNext(_lvMyTracksList.value[idx])
            }
        }
        checkRefreshing()
    }

    fun getAllTrackList() {
        log.e()
        rxDaoSelectAll.onNext(true)
    }

    private fun getIndexById(trackId: String): Int {
        for (index in _lvMyTracksList.value.indices) {
            if (_lvMyTracksList.value[index].trackEntity.trackId == trackId) {
                return index
            }
        }
        return -1
    }

    private fun checkRefreshing() {
        for (item in _lvMyTracksList.value) {
            if (item.isRefreshing.value) {
                lvIsRefreshing.value = true
                return
            }
        }
        lvIsRefreshing.value = false
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_add -> {
                log.e()
                lvStartAddAct.value = Event(true)
            }
            R.id.btn_setting -> {
                log.e()
                lvStartSettingAct.value = Event(true)
            }
        }
    }

    fun onClickItem(item: TrackListItem) {
        log.e()
        lvStartDetailAct.value = Event(item.trackEntity)
    }

    fun onClickTrackId(item: TrackListItem) {
        log.e()
        lvCopyClipboard.value = Event(item.trackEntity.trackId)
    }

    fun onClickDelete(item: TrackListItem) {
        log.e()
        rxDaoDelete.onNext(item.trackEntity)
    }


}