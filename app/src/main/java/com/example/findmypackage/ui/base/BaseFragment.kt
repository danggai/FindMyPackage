package com.example.findmypackage.ui.base

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.findmypackage.R

open class BaseFragment: Fragment() {

    fun getIntent(): Intent {
        return activity?.intent ?: Intent()
    }

    fun makeToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun BaseViewModel.setCommonFun(view: View) {

        lvMakeToast.observe(viewLifecycleOwner, Observer{ msg ->
            activity?.let {
                if (msg.isNotBlank()) makeToast(msg)
            }
        })

        lvCopyClipboard.observe(viewLifecycleOwner, Observer{ trackId ->
            activity?.let {
                val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                val clip = ClipData.newPlainText("label", trackId)
                clipboard?.setPrimaryClip(clip)

                if (trackId.isNotBlank()) makeToast(String.format(getString(R.string.msg_copy_complete), trackId))
            }
        })

    }

}