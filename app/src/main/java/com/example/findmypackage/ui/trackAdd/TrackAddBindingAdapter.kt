package com.example.findmypackage.ui.trackAdd;

import android.widget.GridView
import androidx.databinding.BindingAdapter;
import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.data.res.ResCarrier

import kotlin.jvm.JvmStatic;

object TrackAddBindingAdapter {
    @BindingAdapter(value = ["items", "viewModel"], requireAll = true)
    @JvmStatic fun bindItemList(
        view: GridView,
        items: MutableList<Carrier>,
        vm: TrackAddViewModel
    ) {
        view.adapter?.run {
            if (this is TrackAddAdapter) {
                this.setItemList(items)
            }
        } ?: run {
            TrackAddAdapter(vm).apply {
                view.adapter = this
                this.setItemList(items)
            }
        }
    }

    @BindingAdapter(value = ["clearItem"], requireAll = true)
    @JvmStatic fun bindItemClear(
        view: GridView,
        isClearItem: Boolean
    ) {
        if (isClearItem) {
            view.adapter?.run {
                if (this is TrackAddAdapter) {
                    this.clear()
                }
            }
        }
    }
}
