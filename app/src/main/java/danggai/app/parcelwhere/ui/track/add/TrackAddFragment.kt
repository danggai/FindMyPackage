package danggai.app.parcelwhere.ui.track.add

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import danggai.app.parcelwhere.BindingFragment
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.data.rxbus.RxBusMainSelectAll
import danggai.app.parcelwhere.databinding.TrackAddFragmentBinding
import danggai.app.parcelwhere.extension.onlyEngNum
import danggai.app.parcelwhere.ui.dialog.RxImageDialog
import danggai.app.parcelwhere.ui.track.detail.TrackDetailActivity
import danggai.app.parcelwhere.util.EventObserver
import danggai.app.parcelwhere.util.log
import org.koin.androidx.viewmodel.ext.android.getViewModel

class TrackAddFragment : BindingFragment<TrackAddFragmentBinding>() {

    companion object {
        val TAG: String = TrackAddFragment::class.java.simpleName
        fun newInstance() = TrackAddFragment()
    }

    private lateinit var mVM: TrackAddViewModel

    @LayoutRes
    override fun getLayoutResId() = R.layout.track_add_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm?.let {
            mVM = it
            it.setCommonFun(view)
        }

        FlexboxLayoutManager(activity).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
        }.let {
            binding.rvCarriers.layoutManager = it
        }

        initUi()
        initLv()
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColorWhite()
    }

    private fun initUi() {
        binding.etTrackId.onlyEngNum()
        mVM.initUi()
    }

    private fun initLv() {
        mVM.lvStartDetailAct.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                log.e()
                activity?.let { act ->
                    log.e()
                    RxBusMainSelectAll.getSubject()?.onNext(true)
                    TrackDetailActivity.normalStart(act, TrackEntity(mVM.lvTrackId.value, mVM.lvItemName.value, "", mVM.lvCarrierId.value, "", "", "", "", false))
                    act.finish()
                }
            }
        })
        mVM.lvGoBack.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                log.e()
                activity?.let { act ->
                    log.e()
                    RxBusMainSelectAll.getSubject()?.onNext(true)
                    act.finish()
                }
            }
        })
        mVM.lvDialogAddItemForcibly.observe(viewLifecycleOwner, EventObserver { msg ->
            log.e()
            activity?.let { act ->
                log.e()
                RxImageDialog(RxImageDialog.Builder(act, null, msg, getString(R.string.confirm), getString(R.string.dialog_cancel), false))
                    .show()
                    .subscribe {
                        if (it) {
                            log.e()
                            mVM.forciblyAddItem()
                        } else log.e()
                    }
            }
        })
        mVM.lvProgressVisibility.observe(viewLifecycleOwner, EventObserver { boolean ->
            log.e()
            binding.llProgress.visibility = if (boolean) View.VISIBLE else View.GONE
        })
    }
}