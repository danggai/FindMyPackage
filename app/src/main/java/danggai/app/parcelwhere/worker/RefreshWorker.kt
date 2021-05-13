package danggai.app.parcelwhere.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.data.rxbus.RxBusMainSelectAll
import danggai.app.parcelwhere.util.log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RefreshWorker (context: Context, workerParams: WorkerParameters, private val api: ApiRepository, private val dao: TrackDao) :
    Worker(context, workerParams) {

    private val rxApiCarrierTracks: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private val rxDaoUpdate: PublishSubject<TrackEntity> = PublishSubject.create()

    private val compositeDisposable = CompositeDisposable()

    init {
//        compositeDisposable.addAll (
//            rxApiCarrierTracks
//                .observeOn(Schedulers.newThread())
//                .switchMap {
//                    log.e()
//                    api.carriersTracks(it.first, it.second)
//                }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ res ->
//                    log.e(res)
//                    when (res.meta.code) {
//                        Constant.META_CODE_SUCCESS -> {
//                            rxDaoUpdate.onNext(
//                                TrackEntity(res.data.trackId, "", res.data.from.name, res.data.carrier.id, res.data.carrier.name, res.data.progresses[res.data.progresses.size-1].time, res.data.state.text, false)
//                            )
//                        }
//                        Constant.META_CODE_BAD_REQUEST,
//                        Constant.META_CODE_SERVER_ERROR,
////                    -> {
////                        log.e()
////                        lvMakeToast.value = Event(getString(R.string.msg_network_error))
////                        goBack()
////                    }
//                        Constant.META_CODE_NOT_FOUND -> {
//                            log.e()
//                        }
//                        else -> {
//                            log.e()
//                        }
//                    }
//                }, {
//                    it.message?.let { msg -> log.e(msg) }
//                })
//            ,
//            rxDaoUpdate
//                .observeOn(Schedulers.newThread())
//                .map { item ->
//                    log.e(item)
//                    TrackEntity(item.trackId, dao.selectItemNameById(item.trackId), item.fromName, item.carrierId, item.carrierName, item.recentTime, item.recentStatus, item.isRefreshed)
//                }
//                .map { item ->
//                    dao.update(item)
//                    item
//                }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe ({ item ->
//                    log.e(item)
//                    RxBusMainSelectAll.getSubject()?.onNext(true)
//                }, {
//                    it.message?.let { msg -> log.e(msg) }
//                })
//
//        )
    }

    override fun doWork(): Result {
        log.e()
        try {
            Observable.just(true)
                .observeOn(Schedulers.newThread())
                .switchMap{
                    log.e()
                    dao.selectAll()
                }
                .map { items ->
                    for (item in items) {
                        if (!item.recentStatus!!.contains(Constant.STATE_DELIVERY_COMPLETE)) {
                            rxApiCarrierTracks.onNext(Pair(item.carrierId, item.trackId))
                        }
                    }

                    true
                }
                .subscribe({
                    TODO("개발 중")
                    log.e()
                }, {

                })
            Result.success()
        } catch (e: Exception) {
            log.e(e.message.toString())
            Result.failure()
        }

        return Result.success()
    }

}