package danggai.app.parcelwhere.util

import android.content.Context
import android.content.SharedPreferences
import danggai.app.parcelwhere.Constant


object PreferenceManager {
    const val PREFERENCES_NAME = "danggai.app.parcelwhere"
    private const val DEFAULT_VALUE_STRING = ""
    private const val DEFAULT_VALUE_BOOLEAN = false
    private const val DEFAULT_VALUE_INT = -1
    private const val DEFAULT_VALUE_LONG = -1L
    private const val DEFAULT_VALUE_FLOAT = -1f
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    /**
     * String 값 저장
     * @param context
     * @param key
     * @param value
     */
    fun setString(context: Context, key: String?, value: String?) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * boolean 값 저장
     * @param context
     * @param key
     * @param value
     */
    fun setBoolean(context: Context, key: String?, value: Boolean) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * int 값 저장
     * @param context
     * @param key
     * @param value
     */
    fun setInt(context: Context, key: String?, value: Int) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * long 값 저장
     * @param context
     * @param key
     * @param value
     */
    fun setLong(context: Context, key: String?, value: Long) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * float 값 저장
     * @param context
     * @param key
     * @param value
     */
    fun setFloat(context: Context, key: String?, value: Float) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    /**
     * String 값 로드
     * @param context
     * @param key
     * @return
     */
    fun getString(context: Context, key: String?): String? {
        val prefs = getPreferences(context)
        return prefs.getString(key, DEFAULT_VALUE_STRING)
    }

    /**
     * boolean 값 로드
     * @param context
     * @param key
     * @return
     */
    fun getBoolean(context: Context, key: String?, default: Boolean): Boolean {
        val prefs = getPreferences(context)
        return prefs.getBoolean(key, default)
    }

    /**
     * int 값 로드
     * @param context
     * @param key
     * @return
     */
    fun getInt(context: Context, key: String?): Int {
        val prefs = getPreferences(context)
        return prefs.getInt(key, DEFAULT_VALUE_INT)
    }

    /**
     * long 값 로드
     * @param context
     * @param key
     * @return
     */
    fun getLong(context: Context, key: String?): Long {
        val prefs = getPreferences(context)
        return prefs.getLong(key, DEFAULT_VALUE_LONG)
    }

    fun getLong(context: Context, key: String?, default: Long): Long {
        val prefs = getPreferences(context)
        return prefs.getLong(key, default)
    }

    /**
     * float 값 로드
     * @param context
     * @param key
     * @return
     */
    fun getFloat(context: Context, key: String?): Float {
        val prefs = getPreferences(context)
        return prefs.getFloat(key, DEFAULT_VALUE_FLOAT)
    }

    /**
     * 키 값 삭제
     * @param context
     * @param key
     */
    fun removeKey(context: Context, key: String?) {
        val prefs = getPreferences(context)
        val edit = prefs.edit()
        edit.remove(key)
        edit.apply()
    }

    /**
     * 모든 저장 데이터 삭제
     * @param context
     */
    fun clear(context: Context) {
        val prefs = getPreferences(context)
        val edit = prefs.edit()
        edit.clear()
        edit.apply()
    }


    /**
     * 커스텀 함수
     */
    fun getBooleanIsFirstRun(context: Context): Boolean {
        return getBoolean(context, Constant.PREF_IS_FIRST_RUN, Constant.PREF_DEFAULT_IS_FIRST_RUN)
    }
    fun setBooleanIsFirstRun(context: Context, value: Boolean) {
        setBoolean(context, Constant.PREF_IS_FIRST_RUN, value)
    }

    fun getBooleanNotiWhenAutoRegister(context: Context): Boolean {
        return getBoolean(context, Constant.PREF_NOTI_WHEN_AUTO_REGISTER, Constant.PREF_DEFAULT_NOTI_WHEN_AUTO_REGISTER)
    }
    fun setBooleanNotiWhenAutoRegister(context: Context, value: Boolean) {
        setBoolean(context, Constant.PREF_NOTI_WHEN_AUTO_REGISTER, value)
    }

    fun getBooleanAutoRefresh(context: Context): Boolean {
        return getBoolean(context, Constant.PREF_AUTO_REFRESH, Constant.PREF_DEFAULT_REFRESH)
    }
    fun setBooleanAutoRefresh(context: Context, value: Boolean) {
        setBoolean(context, Constant.PREF_AUTO_REFRESH, value)
    }

    fun getLongAutoRefreshPeriod(context: Context): Long {
        return getLong(context, Constant.PREF_AUTO_REFRESH_PERIOD, Constant.PREF_DEFAULT_REFRESH_PERIOD)
    }
    fun setLongAutoRefreshPeriod(context: Context, value: Long) {
        setLong(context, Constant.PREF_AUTO_REFRESH_PERIOD, value)
    }

    fun getBooleanNotiWhenParcelRefresh(context: Context): Boolean {
        return getBoolean(context, Constant.PREF_NOTI_WHEN_PARCEL_REFRESH, Constant.PREF_DEFAULT_NOTI_WHEN_PARCEL_REFRESH)
    }
    fun setBooleanNotiWhenParcelRefresh(context: Context, value: Boolean) {
        setBoolean(context, Constant.PREF_NOTI_WHEN_PARCEL_REFRESH, value)
    }

}