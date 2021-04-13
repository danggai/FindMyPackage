package com.example.findmypackage.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.example.findmypackage.Constant
import com.example.findmypackage.R
import com.example.findmypackage.data.db.AppDatabase
import com.example.findmypackage.data.db.track.TrackDao
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.ui.track.detail.TrackDetailActivity
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
                rxDaoInsertWithIgnore.onNext(
                    TrackEntity(trackId, itemName, "", carrierId, CarrierUtil.getCarrierName(carrierId), CommonFunction.now(), "")
                )
            }
        }
    }

    private fun sendNoti(item: TrackEntity) {
        log.e()

        if (!PreferenceManager.getBoolean(applicationContext, Constant.PREF_ALLOW_GET_NOTI)) return

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val msg = "${item.itemName} (${CarrierUtil.getCarrierName(item.carrierId)} ${item.trackId}) 이 자동 등록 되었습니다."

        val resultIntent = Intent(this, TrackDetailActivity::class.java)
        resultIntent.putExtra(TrackDetailActivity.ARG_TRACK_ENTITY, item)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        var builder = NotificationCompat.Builder(this, Constant.PUSH_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_truck)
            .setContentText(msg)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(Constant.PUSH_CHANNEL_ID, Constant.PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                    description = Constant.PUSH_CHANNEL_DESC
                }
            )
        }

        notificationManager.notify(0, builder.build())
    }
}