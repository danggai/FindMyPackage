package danggai.app.parcelwhere.ui.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.databinding.DialogTextEditBinding
import danggai.app.parcelwhere.util.log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_text_edit.*

class RxTextEditDialog(builder: Builder) {

    private val mActivity   = builder.activity
    private var mMsg        = builder.msg
    private var mHint       = builder.hint
    private val mConfirm    = builder.confirm
    private val mCancel     = builder.cancel
    private val mConfirmOnly= builder.confirmOnly

    private val mDialog = getDialog()

    private fun getDialog(): TextEditDialog {
        return TextEditDialog(mActivity, mMsg, mHint, mConfirm, mCancel, mConfirmOnly)
    }

    fun show(): Observable<String> {
        val result: PublishSubject<String> = PublishSubject.create()
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
                  internal val hint: String,
                  internal val msg: String,
                  internal val confirm: String,
                  internal val cancel: String,
                  internal val confirmOnly: Boolean) {
        fun build(): RxTextEditDialog = RxTextEditDialog(this)
    }
}

private class TextEditDialog(
    mActivity: Activity,
    private var mMsg: String,
    private var mHint: String,
    private val mConfirm: String,
    private val mCancel: String,
    private val mConfirmOnly: Boolean) : AlertDialog(mActivity) {

    private lateinit var binding: DialogTextEditBinding

    private var mPublishSubject: PublishSubject<String> ?= null
    private var mResult: String = ""

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogTextEditBinding.inflate(layoutInflater)
        
        setContentView(R.layout.dialog_text_edit)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val params = window!!.attributes
        params.gravity = Gravity.CENTER

        setMessage(mMsg)
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
        tv_message.text = mMsg
    }

    fun setHint(hint: String) {
        mHint = hint
        text_input.hint = mHint
    }

    private fun controlBtn() {
        if (mConfirmOnly) {
            tv_cancel.visibility = View.GONE
            tv_confirm.visibility = View.VISIBLE

            tv_confirm.text = mConfirm
            tv_confirm.setOnClickListener {
                log.e(text_input.text.toString())
                mResult = text_input.text.toString()
                dismiss()
            }
        } else {
            tv_cancel.visibility = View.VISIBLE
            tv_confirm.visibility = View.VISIBLE
            tv_confirm.text = mConfirm
            tv_confirm.setOnClickListener {
                log.e()
                mResult = text_input.text.toString()
                dismiss()
            }
            tv_cancel.text = mCancel
            tv_cancel.setOnClickListener {
                log.e()
                mResult = ""
                dismiss()
            }
        }
    }

    fun requestRx(result: PublishSubject<String>) {
        mPublishSubject = result
    }

    private fun sendResult (result: String) {
        log.e()
        mPublishSubject?.onNext(result)

        mPublishSubject?.onComplete()
    }

}
