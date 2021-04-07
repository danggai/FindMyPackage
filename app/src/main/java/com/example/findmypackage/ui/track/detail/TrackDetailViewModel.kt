package com.example.findmypackage.ui.track.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.Constant
import com.example.findmypackage.R
import com.example.findmypackage.ui.base.BaseViewModel
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.db.track.TrackDao
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.data.local.Tracks
import com.example.findmypackage.util.Event
import com.example.findmypackage.util.NonNullMutableLiveData
import com.example.findmypackage.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class TrackDetailViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao)  : BaseViewModel(app) {

    var lvModifyItemName = MutableLiveData<Event<String>>()

    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private val rxDaoUpdate: PublishSubject<TrackEntity> = PublishSubject.create()
    private val rxDaoUpdateNameById: PublishSubject<Pair<String, String>> = PublishSubject.create()

    var lvTrackEntity: NonNullMutableLiveData<TrackEntity> = NonNullMutableLiveData(TrackEntity("","","","","","",""))
    var lvTrackData: NonNullMutableLiveData<Tracks> = NonNullMutableLiveData(Tracks(Tracks.From("",""), Tracks.To("",""), Tracks.State("",""), listOf(), Tracks.Carrier("","","")))

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
                            TrackEntity(lvTrackEntity.value.trackId, "", res.data.from.name, res.data.carrier.id, res.data.carrier.name, res.data.progresses[res.data.progresses.size-1].time, res.data.state.text)
                        )
                    }
                    Constant.META_CODE_BAD_REQUEST,
                    Constant.META_CODE_NOT_FOUND,
                    Constant.META_CODE_SERVER_ERROR -> {
                        lvMakeToast.value = getString(R.string.msg_network_error)
                        lvGoBack.value = Event(true)
                    }
                    else -> {

                    }
                }
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxDaoUpdate
            .observeOn(Schedulers.newThread())
            .subscribe ({ item ->
                log.e(item)
                dao.update(TrackEntity(item.trackId, dao.selectItemNameById(item.trackId), item.fromName, item.carrierId, item.carrierName, item.recentTime, item.recentStatus))
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()

        rxDaoUpdateNameById
            .observeOn(Schedulers.newThread())
            .subscribe ({ item ->
                log.e(item)
                dao.updateNameById(name = item.first, id = item.second)
            }, {
                it.message?.let { msg -> log.e(msg) }
            }).addCompositeDisposable()
    }

    fun initUi(item: TrackEntity) {
        log.e()
        lvTrackEntity.value = item
        rxApiCarrierTracks.onNext(Pair(item.carrierId, item.trackId))
    }

    fun onClickItemName() {
        log.e()
        lvModifyItemName.value = Event(lvTrackEntity.value.itemName)
    }

    fun updateItemName(name: String) {
        log.e()
        rxDaoUpdateNameById.onNext(Pair(name, lvTrackEntity.value.trackId))
    }

}