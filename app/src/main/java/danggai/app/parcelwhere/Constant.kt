package danggai.app.parcelwhere

object Constant {
    const val BASE_URL = "https://apis.tracker.delivery/"

    const val URL_GOOGLE_FORM_NOTI_FAIL = "https://forms.gle/CizyMXQJykaE6RCc7"

    const val META_CODE_SUCCESS = 200
    const val META_CODE_BAD_REQUEST = 400
    const val META_CODE_NOT_FOUND = 404
    const val META_CODE_SERVER_ERROR = 500

    const val BACK_BUTTON_INTERVAL: Long = 2000

    const val PATTERN_ENG_NUM_ONLY = "^[a-zA-Z0-9]+$"
    const val PATTERN_NUM_ONLY = "[^\\d]"

    const val STATE_DELIVERY_COMPLETE = "완료"

    const val DATE_FORMAT_BEFORE = "yyyy-MM-dd'T'HH:mm:ssX"
    const val DATE_FORMAT_AFTER = "yyyy-MM-dd HH:mm"

    const val CLIPBOARD_LABEL_TRACK_ID = "track_id"

    const val WORKER_UNIQUE_NAME_AUTO_REFRESH = "AutoRefreshWork"

    const val PUSH_CHANNEL_ID = "default_id"
    const val PUSH_CHANNEL_NAME= "운송장 추적 알림"
    const val PUSH_CHANNEL_DESC = "앱에서 알림을 통해 운송장을 추적 할 때 수신하게 되는 알림이에요."
    const val PUSH_TITLE_NEW = "신규 운송장 추가"
    const val PUSH_TITLE_EXIST = "운송장 갱신"

    const val PREF_IS_FIRST_RUN = "PREF_IS_FIRST_RUN"
    const val PREF_ALLOW_GET_NOTI = "PREF_ALLOW_GET_NOTI"
    const val PREF_AUTO_REFRESH = "PREF_AUTO_REFRESH"
    const val PREF_AUTO_REFRESH_TERM = "PREF_AUTO_REFRESH_TERM"
}