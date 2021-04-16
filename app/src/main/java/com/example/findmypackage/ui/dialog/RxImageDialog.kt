package com.example.findmypackage.ui.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.findmypackage.R
import com.example.findmypackage.util.log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_default.*

class RxImageDialog(builder: Builder) {

    private val mActivity   = builder.activity
    private val mImage      = builder.img
    private var mMsg        = builder.msg
    private val mConfirm    = builder.confirm
    private val mCancel     = builder.cancel
    private val mConfirmOnly= builder.confirmOnly

    private val mDialog = getDialog()

    private fun getDialog(): ImageDialog {
        return ImageDialog(mActivity, mImage, mMsg, mConfirm, mCancel, mConfirmOnly)
    }

    fun show(): Observable<Boolean> {
        val result: PublishSubject<Boolean> = PublishSubject.create()
        mDialog.setCancelable(false)
        mDialog.requestRx(result)
        try {
            mDialog.show()
        } catch (e: Exception) {
            log.e(e)
        }

        return result
    }

    class Builder(internal val activity: FragmentActivity,
                  internal val img: Int?,
                  internal val msg: String,
                  internal val confirm: String,
                  internal val cancel: String,
                  internal val confirmOnly: Boolean) {
        fun build(): RxImageDialog = RxImageDialog(this)
    }
}

private class ImageDialog(
    mActivity: Activity,
    private var mImage: Int?,
    private var mMsg: String,
    private val mConfirm: String,
    private val mCancel: String,
    private val mConfirmOnly: Boolean) : AlertDialog(mActivity) {

    private var mPublishSubject: PublishSubject<Boolean> ?= null
    private var mResult = false

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_default)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val params = window!!.attributes
        params.gravity = Gravity.CENTER

        setMessage(mMsg)
        setImage(mImage)
        controlBtn()
    }

    override fun dismiss() {
        try {
            sendResult(mResult)
            super.dismiss()
        } catch (e: Exception) {
            e.message?.let { log.e(it) }
        }
    }

    fun setMessage(msg: String) {
        mMsg = msg
        tv_message?.let {
            it.text = mMsg
        }
    }

    fun setImage(img: Int?) {
        mImage = img
        iv_image?.let {
            if (img == null) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.setImageResource(img)
            }
        }
    }

    private fun controlBtn() {
        if (mConfirmOnly) {
            mr_cancel.visibility = View.GONE
            mr_confirm.visibility = View.VISIBLE

            tv_confirm.text = mConfirm
            mr_confirm.setOnClickListener {
                mResult = true
                dismiss()
            }
        } else {
            mr_cancel.visibility = View.VISIBLE
            mr_confirm.visibility = View.VISIBLE
            tv_confirm.text = mConfirm
            mr_confirm.setOnClickListener {
                mResult = true
                dismiss()
            }
            tv_cancel.text = mCancel
            mr_cancel.setOnClickListener {
                mResult = false
                dismiss()
            }
        }
    }

    fun requestRx(result: PublishSubject<Boolean>) {
        mPublishSubject = result
    }

    private fun sendResult (result: Boolean) {
        log.e()
        mPublishSubject?.onNext(result)

        mPublishSubject?.onComplete()
    }

}
