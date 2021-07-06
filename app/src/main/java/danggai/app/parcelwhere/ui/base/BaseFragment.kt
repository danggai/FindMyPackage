package danggai.app.parcelwhere.ui.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.util.EventObserver
import danggai.app.parcelwhere.util.log
import java.lang.Exception

open class BaseFragment: Fragment() {

    fun getIntent(): Intent {
        return activity?.intent ?: Intent()
    }

    fun makeToast(context: Context, msg: String) {
        log.e()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun getAppVersion(): String {

        context?.let {
            try {
                val i: PackageInfo = it.packageManager.getPackageInfo(it.packageName, 0)
                return i.versionName
            } catch (e: PackageManager.NameNotFoundException)
            { }
        }

        return ""
    }

    fun isNotificationPermissionAllowed(): Boolean {
        log.e()
        return context?.let { context ->
            NotificationManagerCompat.getEnabledListenerPackages(context)
                .any { enabledPackageName ->
                    enabledPackageName == context.packageName
                }
        }?: false
    }

    fun startAllowNotiPermission() {
        log.e()
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    fun setStatusBarColorWhite() {
        try {
            val window: Window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.f1)
        } catch (e: IllegalStateException) {
             log.e(e)
        }
    }

    fun setStatusBarColorPrimary() {
        try {
            val window: Window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.c1)
        } catch (e: IllegalStateException) {
             log.e(e)
        }
    }

    fun BaseViewModel.setCommonFun(view: View) {
        lvMakeToast.observe(viewLifecycleOwner, EventObserver { msg ->
            log.e()
            activity?.let {
                if (msg.isNotBlank()) makeToast(it, msg)
            }
        })
    }

}