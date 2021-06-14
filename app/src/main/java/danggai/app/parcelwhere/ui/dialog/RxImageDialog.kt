package danggai.app.parcelwhere.ui.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.databinding.DialogDefaultBinding
import danggai.app.parcelwhere.util.log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

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

    class Builder(
        internal val activity: FragmentActivity,
        internal val img: Int?,
        internal val msg: String,
        internal val confirm: String,
        internal val cancel: String,
        internal val confirmOnly: Boolean,
    ) {
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

    private lateinit var binding: DialogDefaultBinding

    private var mPublishSubject: PublishSubject<Boolean> ?= null
    private var mResult = false

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogDefaultBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

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
        binding.tvMessage.let {
            it.text = mMsg
        }
    }

    fun setImage(img: Int?) {
        mImage = img
        binding.ivImage.let {
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
            binding.mrCancel.visibility = View.GONE
            binding.mrConfirm.visibility = View.VISIBLE

            binding.tvConfirm.text = mConfirm
            binding.mrConfirm.setOnClickListener {
                mResult = true
                dismiss()
            }
        } else {
            binding.mrCancel.visibility = View.VISIBLE
            binding.mrConfirm.visibility = View.VISIBLE
            binding.tvConfirm.text = mConfirm
            binding.mrConfirm.setOnClickListener {
                mResult = true
                dismiss()
            }
            binding.tvCancel.text = mCancel
            binding.mrCancel.setOnClickListener {
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
