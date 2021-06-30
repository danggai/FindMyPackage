package danggai.app.parcelwhere

object Constant {

    /* URL */

    const val BASE_URL = "https://apis.tracker.delivery/"

    const val URL_GOOGLE_FORM_NOTI_FAIL = "https://forms.gle/CizyMXQJykaE6RCc7"



    /* HTTP STATUS CODE */

    const val META_CODE_SUCCESS = 200
    const val META_CODE_BAD_REQUEST = 400
    const val META_CODE_NOT_FOUND = 404
    const val META_CODE_SERVER_ERROR = 500

    const val BACK_BUTTON_INTERVAL: Long = 2000



    /* FORMAT, REGEX */

    const val PATTERN_ENG_NUM_ONLY = "^[a-zA-Z0-9]+$"
    const val PATTERN_NUM_ONLY = "[^\\d]"

    const val STATE_DELIVERY_COMPLETE = "완료"

    const val DATE_FORMAT_BEFORE = "yyyy-MM-dd'T'HH:mm:ssX"
    const val DATE_FORMAT_AFTER = "yyyy-MM-dd HH:mm"

    const val CLIPBOARD_LABEL_TRACK_ID = "track_id"



    /* WORKER */

    const val WORKER_UNIQUE_NAME_AUTO_REFRESH = "AutoRefreshWork"

    const val WORKER_DATA_REFRESH_PERIOD = "refresh_period"



    /* ID */

    const val PUSH_CHANNEL_ID = "default_id"
    const val PUSH_CHANNEL_NAME= "운송장 추적 알림"
    const val PUSH_CHANNEL_DESC = "앱에서 알림을 통해 운송장을 추적 할 때 수신하게 되는 알림이에요."
    const val PUSH_TITLE_NEW = "신규 운송장 추가"
    const val PUSH_TITLE_EXIST = "운송장 갱신"



    /* PREFERENCE */

    const val PREF_IS_FIRST_RUN = "PREF_IS_FIRST_RUN"
    const val PREF_NOTI_WHEN_AUTO_REGISTER = "PREF_NOTI_WHEN_AUTO_REGISTER"
    const val PREF_AUTO_REFRESH = "PREF_AUTO_REFRESH"
    const val PREF_AUTO_REFRESH_PERIOD = "PREF_AUTO_REFRESH_PERIOD"
    const val PREF_NOTI_WHEN_PARCEL_REFRESH = "PREF_PARCEL_REFRESH_RECEIVE_NOTI"

    const val PREF_DEFAULT_IS_FIRST_RUN = true
    const val PREF_DEFAULT_NOTI_WHEN_AUTO_REGISTER = true
    const val PREF_DEFAULT_REFRESH = true
    const val PREF_DEFAULT_REFRESH_PERIOD = 60L
    const val PREF_DEFAULT_NOTI_WHEN_PARCEL_REFRESH = true


    /* ARGUMENT */

    const val ARG_IS_ONE_TIME = "ARG_IS_ONE_TIME"
}