package danggai.app.parcelwhere.worker

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.data.rxbus.RxBusMainSelectAll
import danggai.app.parcelwhere.ui.track.detail.TrackDetailActivity
import danggai.app.parcelwhere.util.CarrierUtil
import danggai.app.parcelwhere.util.CommonFunction
import danggai.app.parcelwhere.util.PreferenceManager
import danggai.app.parcelwhere.util.log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class RefreshWorker (context: Context, workerParams: WorkerParameters, private val api: ApiRepository, private val dao: TrackDao) :
    CoroutineWorker(context, workerParams) {

    private suspend fun apiCarrierTracks(item: TrackEntity) {
        log.e()
        val res = api.suspendCarriersTracks(item.carrierId, item.trackId)

        log.e(res)
        when (res.meta.code) {
            Constant.META_CODE_SUCCESS -> {
                if (item.recentStatus != res.data.state.text) {
                    log.e()
                    // 알람 전송
                    sendNoti(item)
                }
                val isRefreshed = item.recentTime != res.data.progresses[res.data.progresses.lastIndex].time
                daoUpdate(TrackEntity(res.data.trackId, "", res.data.from.name, res.data.carrier.id, res.data.carrier.name, res.data.progresses[res.data.progresses.size-1].time, res.data.state.text, isRefreshed))
            }
            Constant.META_CODE_BAD_REQUEST,
            Constant.META_CODE_SERVER_ERROR,
            Constant.META_CODE_NOT_FOUND -> {
                log.e()
            }
            else -> {
                log.e()
            }
        }
    }

    private suspend fun daoUpdate(item: TrackEntity) {
        val updatedItem = TrackEntity(item.trackId, dao.suspendSelectItemNameById(item.trackId), item.fromName, item.carrierId, item.carrierName, item.recentTime, item.recentStatus, item.isRefreshed)
        dao.suspendUpdate(updatedItem)

        log.e(updatedItem)
        RxBusMainSelectAll.getSubject()?.onNext(true)
    }

    private fun sendNoti(item: TrackEntity) {
        log.e()

        if (!PreferenceManager.getBooleanAllowGetNoti(applicationContext)) return

        val title = applicationContext.getString(R.string.push_title_exist)
        val msg = "${item.itemName} (${CarrierUtil.getCarrierName(item.carrierId)} ${item.trackId}) 상품이 '${item.recentStatus}' 상태가 되었습니다."

        val resultIntent = Intent(applicationContext, TrackDetailActivity::class.java)
            .putExtra(TrackDetailActivity.ARG_TRACK_ENTITY, item)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        CommonFunction.sendNotification(item.trackId.substring(0,9).toInt(), applicationContext, resultPendingIntent, title, msg)
    }

    override suspend fun doWork(): Result {
        log.e()
        try {
            val refreshDelay = inputData.getLong(Constant.WORKER_DATA_REFRESH_PERIOD, Constant.PREF_DEFAULT_REFRESH_PERIOD)
            delay((refreshDelay - 1L)*60000)

            coroutineScope {
                val trackList: List<TrackEntity> = dao.suspendSelectAll()
                log.e(trackList)

                for (item in trackList) {
//                    if (true) {
                    if (!item.recentStatus!!.contains(Constant.STATE_DELIVERY_COMPLETE)) {
                        log.e()
                        apiCarrierTracks(item)
                    }
                }
            }

            return Result.success()
        } catch (e: java.lang.Exception) {
            log.e(e.message.toString())
            return Result.failure()
        }
    }

}