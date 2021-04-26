package danggai.app.parcelwhere.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.db.AppDatabase
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.ui.track.detail.TrackDetailActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MyNotificationListenerService: NotificationListenerService() {

    private val db = AppDatabase.getInstance(this)
    private val dao: TrackDao = db.trackDao()

    private val rxDaoInsertWithIgnore: PublishSubject<TrackEntity> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add (
            rxDaoInsertWithIgnore
                .observeOn(Schedulers.newThread())
                .subscribe ({ item ->
                    dao.insertWithIgnore(item)
                    sendNoti(item)
                    log.e(item)
                }, {
                    it.message?.let { msg -> log.e(msg) }
                })
        )
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        log.e("MyNotificationListener.onListenerConnected()")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        log.e("MyNotificationListener.onListenerDisconnected()")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn?.packageName == application.packageName) return
        sbn?.notification?.let {
            val text: CharSequence = it.extras.getCharSequence(Notification.EXTRA_TEXT)?:""

            val carrierId: String = CarrierUtil.getCarrierId(text.toString())
            val trackId: String = CommonFunction.getTrackId(text.toString())
            val itemName: String = CommonFunction.getItemName(text.toString())

            log.e("itemName = $itemName, carrierId = $carrierId, trackId = $trackId")
            if ((trackId.length in 9..14 && CarrierUtil.checkCarrierId(carrierId))) {
                log.e()
                rxDaoInsertWithIgnore.onNext(
                    TrackEntity(trackId, itemName, "", carrierId, CarrierUtil.getCarrierName(carrierId), CommonFunction.now(), "")
                )
            }
        }
    }

    private fun sendNoti(item: TrackEntity) {
        log.e()

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (!PreferenceManager.getBooleanDefaultTrue(applicationContext, Constant.PREF_ALLOW_GET_NOTI)) return

        val msg = "${item.itemName} (${CarrierUtil.getCarrierName(item.carrierId)} ${item.trackId}) 이 자동 등록 되었습니다."

        val resultIntent = Intent(this, TrackDetailActivity::class.java)
            .putExtra(TrackDetailActivity.ARG_TRACK_ENTITY, item)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
//            getPendingIntent(item.trackId.toInt(), PendingIntent.FLAG_UPDATE_CURRENT)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        CommonFunction.sendNotification(0, this, notificationManager, resultPendingIntent, msg)
//        CommonFunction.sendNotification(item.trackId.toInt(), this, notificationManager, resultPendingIntent, msg)

    }
}