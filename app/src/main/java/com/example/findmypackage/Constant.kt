package com.example.findmypackage

object Constant {
    const val BASE_URL = "https://apis.tracker.delivery/"

    const val META_CODE_SUCCESS = 200
    const val META_CODE_BAD_REQUEST = 400
    const val META_CODE_NOT_FOUND = 404
    const val META_CODE_SERVER_ERROR = 500

    const val PATTERN_ENG_NUM_ONLY = "^[a-zA-Z0-9]+$"
    const val PATTERN_NUM_ONLY = "[^\\d]"


    const val STATE_DELIVERY_COMPLETE = "완료"

    const val DATE_FORMAT_BEFORE = "yyyy-MM-dd'T'HH:mm:ssX"
    const val DATE_FORMAT_AFTER = "yyyy-MM-dd HH:mm"

    const val CLIPBOARD_LABEL_TRACK_ID = "track_id"

    const val PUSH_CHANNEL_ID = "default"
    const val PUSH_CHANNEL_NAME= "default"
    const val PUSH_CHANNEL_DESC = "default"
    const val PUSH_TITLE_NEW = "신규 운송장 추가"
    const val PUSH_TITLE_EXIST = "운송장 갱신"
}