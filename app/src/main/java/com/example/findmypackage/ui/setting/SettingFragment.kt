package com.example.findmypackage.ui.setting

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.example.findmypackage.BindingFragment
import com.example.findmypackage.R
import com.example.findmypackage.databinding.SettingFragmentBinding
import com.example.findmypackage.ui.dialog.RxImageDialog
import com.example.findmypackage.util.EventObserver
import com.example.findmypackage.util.log
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SettingFragment : BindingFragment<SettingFragmentBinding>() {

    companion object {
        val TAG: String = SettingFragment::class.java.simpleName
        fun newInstance() = SettingFragment()
    }

    private lateinit var mVM: SettingViewModel

    @LayoutRes
    override fun getLayoutResId() = R.layout.setting_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm?.let {
            mVM = it
            it.setCommonFun(view)
        }

        initLv()
    }

    override fun onResume() {
        super.onResume()
        initUi()
    }

    private fun initUi() {
        context?.let { mVM.lvIsAllowNotiPermission.value = isNotificationPermissionAllowed() }
    }

    private fun initLv() {
        mVM.lvStartNotiSetting.observe(viewLifecycleOwner, EventObserver { allowed ->
            log.e()
            activity?.let { act ->
                val mRxImageDialog = RxImageDialog(RxImageDialog.Builder(act, R.drawable.access_allow_example, getString(R.string.dialog_noti_allow_help), getString(R.string.confirm), getString(R.string.cancel), false))
                mRxImageDialog.show()
                    .subscribe {
                        if (it) startNotificationSetting()
                    }
            }
        })
    }
}