package com.example.findmypackage.ui.main

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.example.findmypackage.BindingFragment
import com.example.findmypackage.Constant
import com.example.findmypackage.R
import com.example.findmypackage.data.rxbus.RxBusMainRefresh
import com.example.findmypackage.databinding.MainFragmentBinding
import com.example.findmypackage.ui.dialog.RxImageDialog
import com.example.findmypackage.ui.setting.SettingActivity
import com.example.findmypackage.ui.track.add.TrackAddActivity
import com.example.findmypackage.ui.track.detail.TrackDetailActivity
import com.example.findmypackage.util.EventObserver
import com.example.findmypackage.util.PreferenceManager
import com.example.findmypackage.util.log
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainFragment : BindingFragment<MainFragmentBinding>() {

    companion object {
        val TAG: String = MainFragment::class.java.simpleName
        fun newInstance() = MainFragment()
    }

    private lateinit var mVM: MainViewModel

    @LayoutRes
    override fun getLayoutResId() = R.layout.main_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        RxBusMainRefresh.getSubject()?.let { event ->
            event.subscribe {
                log.e()
                if (it) mVM.getAllTrackList()
            }
        }

        if (PreferenceManager.getBooleanDefaultTrue(context!!, Constant.PREF_IS_FIRST_RUN)) {
            log.e()
            activity?.let { act ->
                RxImageDialog(RxImageDialog.Builder(act, R.drawable.help_access_noti_allow, getString(R.string.dialog_allow_noti_help_first), getString(R.string.confirm), getString(R.string.denied), false))
                    .show()
                    .subscribe { confirm ->
                        if (confirm) startAllowNotiPermission()
                        PreferenceManager.setBoolean(context!!, Constant.PREF_IS_FIRST_RUN, false)
                    }
            }
        }
    }

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

    override fun onDestroy() {
        super.onDestroy()
        RxBusMainRefresh.release()
    }

    private fun initUi() {
        mVM.initUI()
    }

    private fun initLv() {
        mVM.lvStartAddAct.observe(viewLifecycleOwner, EventObserver{
            if (it) {
                log.d()
                activity?.let {act -> TrackAddActivity.normalStart(act)}
            }
        })
        mVM.lvStartSettingAct.observe(viewLifecycleOwner, EventObserver{
            if (it) {
                log.d()
                activity?.let {act -> SettingActivity.normalStart(act)}
            }
        })
        mVM.lvStartDetailAct.observe(viewLifecycleOwner, EventObserver{ item ->
            log.d()
            activity?.let {act -> TrackDetailActivity.normalStart(act, item)}
        })
    }

}