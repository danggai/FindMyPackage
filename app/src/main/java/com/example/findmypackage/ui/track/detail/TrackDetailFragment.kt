package com.example.findmypackage.ui.track.detail

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.annotation.LayoutRes
import com.example.findmypackage.BindingFragment
import com.example.findmypackage.Constant
import com.example.findmypackage.R
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.databinding.TrackDetailFragmentBinding
import com.example.findmypackage.ui.dialog.RxImageDialog
import com.example.findmypackage.util.EventObserver
import com.example.findmypackage.util.PreferenceManager
import com.example.findmypackage.util.log
import org.koin.androidx.viewmodel.ext.android.getViewModel

class TrackDetailFragment : BindingFragment<TrackDetailFragmentBinding>() {

    companion object {
        val TAG: String = TrackDetailFragment::class.java.simpleName
        fun newInstance() = TrackDetailFragment()
    }

    private lateinit var mVM: TrackDetailViewModel

    @LayoutRes
    override fun getLayoutResId() = R.layout.track_detail_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm?.let {
            mVM = it
            it.setCommonFun(view)
        }

        initUi()
        initLv()
    }

    private fun initUi() {
        getIntent().getParcelableExtra<TrackEntity>(TrackDetailActivity.ARG_TRACK_ENTITY)?.let { item -> mVM.initUi(item) }
    }

    private fun initLv() {
        mVM.lvModifyItemName.observe(viewLifecycleOwner, EventObserver { itemName ->
            val input = EditText(activity)
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.setText(itemName)

            AlertDialog.Builder(activity)
                .setTitle(getString(R.string.dialog_name_change_title))
                .setMessage(getString(R.string.dialog_name_change_msg))
                .setView(input)
                .setPositiveButton(getString(R.string.dialog_submit)) { dialog, whichButton ->
                    makeToast(getString(R.string.dialog_name_change_toast_done))
                    mVM.updateItemName(input.text.toString())
                }
                .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, which -> dialog.cancel() }
                .setCancelable(false)
                .show()
        })

        mVM.lvGoBack.observe(viewLifecycleOwner, EventObserver { it ->
            if (it) activity?.onBackPressed()
        })

        mVM.lvParcelNotFound.observe(viewLifecycleOwner, EventObserver {
            activity?.let { act ->
                RxImageDialog(RxImageDialog.Builder(act, null, getString(R.string.dialog_parcel_not_found), getString(R.string.confirm), getString(R.string.dialog_cancel), false))
                    .show()
                    .subscribe {
                        if (it) {
                            log.e()
                            mVM.deleteItem()
                        } else mVM.goBack()
                    }
            }
        })
    }
}
