package danggai.app.parcelwhere.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.data.api.ApiInterface
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.data.db.AppDatabase
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.ui.track.detail.TrackDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MyNotificationListenerService: NotificationListenerService() {

    private val db = AppDatabase.getInstance(this)
    private val dao: TrackDao = db.trackDao()

    private val rxDaoInsertWithIgnore: PublishSubject<TrackEntity> = PublishSubject.create()
    private val rxDaoExistById: PublishSubject<TrackEntity> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    private var newTrackEntity: NonNullMutableLiveData<TrackEntity> = NonNullMutableLiveData(TrackEntity("","","","","","","",false))

    init {
        compositeDisposable.addAll (
            rxDaoInsertWithIgnore
                .observeOn(Schedulers.newThread())
                .subscribe ({ item ->
                    dao.insertWithIgnore(item)
                    sendNoti(item)
                    log.e(item)
                }, {
                    it.message?.let { msg -> log.e(msg) }
                }),

            rxDaoExistById
                .observeOn(AndroidSchedulers.mainThread())
                .filter { track ->
                    newTrackEntity.value = track
                    true
                }
                .observeOn(Schedulers.newThread())
                .switchMap { track ->
                    log.e(track)
                    dao.existById(track.trackId)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ exist ->
                    if (exist) {
                        log.e()
                        // TODO(이후 이미 등록 된 아이템은 새로고침하여 내역 변동 시 알림)
                    } else {
                        log.e()
                        rxDaoInsertWithIgnore.onNext(newTrackEntity.value)
                    }
                }, {
                    it.message?.let { msg -> log.e(msg) }
                }),
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
        if (sbn!!.notification.flags and Notification.FLAG_GROUP_SUMMARY != 0
            || sbn?.packageName == application.packageName) {
            return
        }
        sbn?.notification?.let {
            val text: CharSequence = it.extras.getCharSequence(Notification.EXTRA_TEXT)?:""

            val carrierId: String = CarrierUtil.getCarrierId(text.toString())
            val trackId: String = CommonFunction.getTrackId(text.toString())
            val itemName: String = CommonFunction.getItemName(text.toString())

            log.e("itemName = $itemName, carrierId = $carrierId, trackId = $trackId")
            if ((trackId.length in 9..14 && CarrierUtil.checkCarrierId(carrierId))) {
                log.e()
                val track = TrackEntity(trackId, itemName, "", carrierId, CarrierUtil.getCarrierName(carrierId), CommonFunction.now(), "", true)

                rxDaoExistById.onNext(track)
//                rxDaoInsertWithIgnore.onNext(
//                    TrackEntity(trackId, itemName, "", carrierId, CarrierUtil.getCarrierName(carrierId), CommonFunction.now(), "")
//                )
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