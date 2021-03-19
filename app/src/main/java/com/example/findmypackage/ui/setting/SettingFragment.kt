package com.example.findmypackage.ui.setting

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.example.findmypackage.BindingFragment
import com.example.findmypackage.R
import com.example.findmypackage.databinding.SettingFragmentBinding
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

        initUi()
        initLv()
    }

    private fun initUi() {
        context?.let { mVM.lvIsAllowNotiPermission.value = isNotificationPermissionAllowed() }
    }

    private fun initLv() {

    }
}