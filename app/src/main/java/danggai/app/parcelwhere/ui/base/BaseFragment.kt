package danggai.app.parcelwhere.ui.base

import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.util.log

open class BaseFragment: Fragment() {

    fun getIntent(): Intent {
        return activity?.intent ?: Intent()
    }

    fun makeToast(msg: String) {
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
        return NotificationManagerCompat.getEnabledListenerPackages(context!!)
            .any { enabledPackageName ->
                log.e(enabledPackageName)
                enabledPackageName == context?.packageName
            }
    }

    fun startAllowNotiPermission() {
        log.e()
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    fun BaseViewModel.setCommonFun(view: View) {

        lvMakeToast.observe(viewLifecycleOwner, Observer { msg ->
            activity?.let {
                if (msg.isNotBlank()) makeToast(msg)
            }
        })

        lvCopyClipboard.observe(viewLifecycleOwner, Observer { trackId ->
            activity?.let {
                val clipboard: ClipboardManager = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard?.addPrimaryClipChangedListener {
                    if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true) {
//                        makeToast(String.format(getString(R.string.msg_copy_complete), trackId))
                        makeToast(getString(R.string.msg_copy_complete_temp))
                    }
                }

                val clip = ClipData.newPlainText(Constant.CLIPBOARD_LABEL_TRACK_ID, trackId)
                clipboard?.setPrimaryClip(clip)
            }
        })

    }

}