package com.example.findmypackage.ui.track.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.findmypackage.Constant
import com.example.findmypackage.ui.base.BaseViewModel
import com.example.findmypackage.data.api.ApiRepository
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.data.local.Tracks
import com.example.findmypackage.util.log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class TrackDetailViewModel(override val app: Application, private val api: ApiRepository) : BaseViewModel(app) {

    var lvTest: MutableLiveData<String> = MutableLiveData("")
    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()

    var lvTrackId : MutableLiveData<String> = MutableLiveData("")
    var lvTrackEntity: MutableLiveData<TrackEntity> = MutableLiveData(TrackEntity("","","","","","",""))

    var lvTrackData: MutableLiveData<Tracks> = MutableLiveData(Tracks(Tracks.From("",""), Tracks.To("",""), Tracks.State("",""), listOf(), Tracks.Carrier("","","")))

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
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
                        }
                        Constant.META_CODE_BAD_REQUEST,
                        Constant.META_CODE_NOT_FOUND,
                        Constant.META_CODE_SERVER_ERROR -> {

                        }
                        else -> {

                        }


                    }
                }, {
                    it.message?.let { msg -> log.e(msg) }
                })
        )

    }

    fun initUi(carrierId: String, trackId: String) {
        log.e()
        lvTrackId.value = trackId
        rxApiCarrierTracks.onNext(Pair(carrierId, trackId))
    }

    fun initUi(item: TrackEntity) {
        log.e()
        lvTrackEntity.value = item
        rxApiCarrierTracks.onNext(Pair(item.carrierId, item.trackId ))
    }

}