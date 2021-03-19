package com.example.findmypackage.ui.setting

import android.widget.CompoundButton
import androidx.databinding.BindingAdapter
import com.example.findmypackage.util.log
import com.google.android.material.switchmaterial.SwitchMaterial

object SettingBindingAdapter {
    @BindingAdapter(value = ["viewModel"], requireAll = true)
    @JvmStatic fun bindItemList(
        view: SwitchMaterial,
        vm: SettingViewModel
    ) {
        view.isChecked = vm.lvIsAllowNotiPermission.value
        view.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                log.e()
            } else {
                log.e()
            }
        }
    }
}