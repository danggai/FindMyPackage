package com.example.findmypackage.ui.base

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

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

    }

}