package com.example.findmypackage.ui.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.findmypackage.R
import com.example.findmypackage.databinding.DialogDefaultBinding
import com.example.findmypackage.util.log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxImageDialog(builder: Builder) {

    private val mActivity   = builder.activity
    private val mImage      = builder.img
    private var mMsg        = builder.msg
    private val mConfirm    = builder.confirm
    private val mCancel     = builder.cancel
    private val mCancelable = builder.cancelable

    private val mDialog = getDialog()

    private fun getDialog(): ImageDialog {
        return ImageDialog(mActivity, mImage, mMsg, mConfirm, mCancel, mCancelable)
    }

    fun show(): Observable<Boolean> {
        val result: PublishSubject<Boolean> = PublishSubject.create()
        mDialog.requestRx(result)
        try {
            mDialog.show()
        } catch (e: Exception) {
            log.e(e)
        }

        return result
    }

    class Builder (internal val activity: FragmentActivity,
                   internal val img: Int,
                   internal val msg: String,
                   internal val confirm: String,
                   internal val cancel: String,
                   internal val cancelable: Boolean) {
        fun build(): RxImageDialog = RxImageDialog(this)
    }
}

private class ImageDialog (
    mActivity: Activity,
    private var mImage: Int,
    private var mMsg: String,
    private val mConfirm: String,
    private val mCancel: String,
    private val mCancelable: Boolean) : AlertDialog(mActivity) {

    private lateinit var binding: DialogDefaultBinding

    private var mPublishSubject: PublishSubject<Boolean> ?= null
    private var mResult = false
    private var mNegative = false

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogDefaultBinding.inflate(layoutInflater)

        setContentView(R.layout.dialog_default)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setImage(mImage)
        setMessage(mMsg)

        val params = window!!. attributes
        params.gravity = Gravity.CENTER

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
        binding.tvMessage?.let {
            it.text = mMsg
        }
    }

    fun setImage(img: Int) {
        mImage = img
        binding.ivImage?.let {
            it.setImageResource(img)
        }
    }

    private fun controlBtn() {
        if (!mCancelable) {
            binding.tvCancel.visibility = View.GONE
            binding.tvConfirm.visibility = View.VISIBLE
            binding.tvConfirm.text = mConfirm

            binding.tvConfirm.setOnClickListener {
                mResult = true
                dismiss()
            }
        } else {
            binding.tvConfirm.text = mConfirm
            binding.tvCancel.text = mCancel
            binding.tvCancel.setOnClickListener {
                mResult = false
                dismiss()
            }
            binding.tvConfirm.setOnClickListener {
                mResult = true
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
