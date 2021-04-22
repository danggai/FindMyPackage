package danggai.app.parcelwhere.ui.setting

import android.app.Application
import androidx.lifecycle.MutableLiveData
import danggai.app.parcelwhere.ui.base.BaseViewModel
import danggai.app.parcelwhere.data.api.ApiRepository
import danggai.app.parcelwhere.data.db.track.TrackDao
import danggai.app.parcelwhere.util.Event
import danggai.app.parcelwhere.util.NonNullMutableLiveData
import danggai.app.parcelwhere.util.log


class SettingViewModel(override val app: Application, private val api: ApiRepository, private val dao: TrackDao) : BaseViewModel(app) {

    var lvStartAccessNotiSetting = MutableLiveData<Event<Boolean>>()
    var lvIsAllowAccessNoti: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)         // denied: -1, granted: 0

    var lvStartGetNotiSetting = MutableLiveData<Event<Boolean>>()
    var lvIsAllowGetNoti: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)

    var lvStartNotiFailForm = MutableLiveData<Event<Boolean>>()

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

}