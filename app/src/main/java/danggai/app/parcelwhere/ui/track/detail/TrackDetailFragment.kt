package danggai.app.parcelwhere.ui.track.detail

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.annotation.LayoutRes
import danggai.app.parcelwhere.BindingFragment
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.databinding.TrackDetailFragmentBinding
import danggai.app.parcelwhere.ui.dialog.RxImageDialog
import danggai.app.parcelwhere.util.EventObserver
import danggai.app.parcelwhere.util.log
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

     override fun onResume() {
        super.onResume()
        setStatusBarColorPrimary()
    }

    private fun initUi() {
        getIntent().getParcelableExtra<TrackEntity>(TrackDetailActivity.ARG_TRACK_ENTITY)?.let { item -> mVM.initUi(item) }
    }

    private fun initLv() {
        mVM.lvModifyItemName.observe(viewLifecycleOwner, EventObserver { itemName ->
            activity?.let { act ->
                val input = EditText(act)
                input.inputType = InputType.TYPE_CLASS_TEXT
                input.setText(itemName)

                AlertDialog.Builder(act)
                    .setTitle(getString(R.string.dialog_name_change_title))
                    .setMessage(getString(R.string.dialog_name_change_msg))
                    .setView(input)
                    .setPositiveButton(getString(R.string.dialog_submit)) { dialog, whichButton ->
                        makeToast(act, getString(R.string.dialog_name_change_toast_done))
                        mVM.updateItemName(input.text.toString())
                    }
                    .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, which -> dialog.cancel() }
                    .setCancelable(false)
                    .show()
            }
        })

        mVM.lvGoBack.observe(viewLifecycleOwner, EventObserver { it ->
            if (it) activity?.onBackPressed()
        })

        mVM.lvParcelNotFound.observe(viewLifecycleOwner, EventObserver { msg ->
            activity?.let { act ->
                RxImageDialog(RxImageDialog.Builder(act, null, msg, getString(R.string.confirm), getString(R.string.dialog_cancel), false))
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
