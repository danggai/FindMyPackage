package com.example.findmypackage.ui.base

import android.content.*
import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.MimeTypeFilter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.findmypackage.Constant
import com.example.findmypackage.R

open class BaseFragment: Fragment() {

    fun getIntent(): Intent {
        return activity?.intent ?: Intent()
    }

    fun makeToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun isNotificationPermissionAllowed(): Boolean {
        return NotificationManagerCompat.getEnabledListenerPackages(context!!)
            .any { enabledPackageName ->
                enabledPackageName == context?.packageName
            }
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
//                clipboard?.addPrimaryClipChangedListener {
//                    if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true) {
//                        makeToast(String.format(getString(R.string.msg_copy_complete), trackId))
//                    }
//                }

                val clip = ClipData.newPlainText(Constant.CLIPBOARD_LABEL_TRACK_ID, trackId)
                clipboard?.setPrimaryClip(clip)
            }
        })

    }

}