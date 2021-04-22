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
import danggai.app.parcelwhere.ui.base.BaseViewModel
import danggai.app.parcelwhere.util.Event
import danggai.app.parcelwhere.util.NonNullMutableLiveData
import danggai.app.parcelwhere.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class MainViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartAddAct = MutableLiveData<Event<Boolean>>()
    var lvStartSettingAct = MutableLiveData<Event<Boolean>>()
    var lvStartDetailAct = MutableLiveData<Event<TrackEntity>>()
    var lvIsRefresh: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)
    var lvIsFirstInit: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(true)

    private val rxApiCarrier: PublishSubject<Boolean> = PublishSubject.create()
    private val rxApiCarrierTracks: PublishSubject<TrackEntity> = PublishSubject.create()
    private val rxDaoSelectAll: PublishSubject<Boolean> = PublishSubject.create()
    private val rxDaoUpdate: PublishSubject<TrackEntity> = PublishSubject.create()
    private val rxDaoDelete: PublishSubject<TrackEntity> = PublishSubject.create()

    private var _lvMyTracksList: NonNullMutableLiveData<List<TrackEntity>> = NonNullMutableLiveData(listOf())
    val lvMyTracksList = _lvMyTracksList

    private var _mTrackId: String = ""
    private var lvRefreshCount: NonNullMutableLiveData<Int> = NonNullMutableLiveData(0)

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
                dao.selectAll()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ items ->
                log.e(items)
                _lvMyTracksList.value = items
                if (lvRefreshSwitch.value) {
                    lvRefreshSwitch.value = false
                    refreshAll()
                }
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxDaoUpdate
            .observeOn(Schedulers.newThread())
            .subscribe ({ item ->
                log.e("refreshed item.trackId -> ${item.trackId}")
                dao.update(
                    TrackEntity(item.trackId, dao.selectItemNameById(item.trackId), item.fromName, item.carrierId, item.carrierName, item.recentTime, item.recentStatus)
                )
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
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                val idx = lvMyTracksList.value.indexOf(it)
                if (lvMyTracksList.value[idx].recentStatus!!.contains(Constant.STATE_DELIVERY_COMPLETE)) {
                    checkRefreshing()
                    false
                } else true
            }
            .observeOn(Schedulers.newThread())
            .switchMap {
                _mTrackId = it.trackId
                api.carriersTracks(it.carrierId, it.trackId)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                when (res.meta.code) {
                    Constant.META_CODE_SUCCESS -> {
                        log.e(res.data)
                        rxDaoUpdate.onNext(
                            TrackEntity(_mTrackId, "", res.data.from.name, res.data.carrier.id, res.data.carrier.name, res.data.progresses[res.data.progresses.size-1].time, res.data.state.text)
                        )
                    }
                    Constant.META_CODE_BAD_REQUEST,
                    Constant.META_CODE_NOT_FOUND,
                    Constant.META_CODE_SERVER_ERROR -> {
                    }
                    else -> {
                    }
                }
                checkRefreshing()
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()
    }

    fun getAllTrackList() {
        log.e()
        rxDaoSelectAll.onNext(true)
    }

    fun initUI() {
        if (!lvIsFirstInit.value) return else lvIsFirstInit.value = false
        log.e()
        lvRefreshSwitch.value = true
        getAllTrackList()
        rxApiCarrier.onNext(true)
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

    fun onClickItem(item: TrackEntity) {
        log.e()
        lvStartDetailAct.value = Event(item)
    }

    fun onClickTrackId(item: TrackEntity) {
        log.e()
        lvCopyClipboard.value = item.trackId
    }

    fun onClickDelete(item: TrackEntity) {
        log.e()
        rxDaoDelete.onNext(item)
    }

    fun refreshAll() {
        log.e()
        lvRefreshCount.value = lvMyTracksList.value.size

        for (item in lvMyTracksList.value) {
            rxApiCarrierTracks.onNext(item)
        }
    }

    private fun checkRefreshing() {
        if (--lvRefreshCount.value == 0) {
            log.e()
            lvIsRefresh.value = false
        }
    }

}