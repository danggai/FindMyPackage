package danggai.app.parcelwhere.ui.setting

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.ui.base.BaseViewModel
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.util.Event
import danggai.app.parcelwhere.util.NonNullMutableLiveData
import danggai.app.parcelwhere.util.log


class SettingViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartAccessNotiSetting = MutableLiveData<Event<Boolean>>()
    var lvIsAllowAccessNoti: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)         // denied: -1, granted: 0
    var lvIsAllowAutoRefresh: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)         // denied: -1, granted: 0
    var lvAutoRefreshPeriod: NonNullMutableLiveData<Int> = NonNullMutableLiveData(15)         // denied: -1, granted: 0

    var lvStartGetNotiSetting = MutableLiveData<Event<Boolean>>()
    var lvIsAllowGetNoti: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)

    var lvStartNotiFailForm = MutableLiveData<Event<Boolean>>()

    var lvSetAutoRefresh = MutableLiveData<Event<Boolean>>()
    var lvSetAutoRefreshPeriod = MutableLiveData<Event<Int>>()

    var lvAppVersion: NonNullMutableLiveData<String> = NonNullMutableLiveData("1.0.0")

    init {

    }

    fun onClickAccessNotiSwitch() {
        log.e()
        lvStartAccessNotiSetting.value = Event(true)
    }

    fun onClickGetNotiSwitch() {
        log.e()
        lvStartGetNotiSetting.value = Event(lvIsAllowGetNoti.value)
    }

    fun onClickNotiReadFail() {
        log.e()
        lvStartNotiFailForm.value = Event(true)
    }

    fun onClickSetAutoRefresh() {
        log.e()
        lvSetAutoRefresh.value = Event(lvIsAllowAutoRefresh.value)
    }

    fun onClickSetAutoRefreshPeriod(view: View) {
        log.e()
        val period = when (view.id) {
            R.id.rb_15m -> 15
            R.id.rb_30m -> 30
            R.id.rb_1h -> 60
            R.id.rb_2h -> 120
            else -> -1
        }

        lvSetAutoRefreshPeriod.value = Event(period)
    }
}