package com.example.findmypackage.ui.track.add;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.findmypackage.data.local.Carrier

import kotlin.jvm.JvmStatic;

object TrackAddBindingAdapter {
    @BindingAdapter(value = ["items", "viewModel"], requireAll = true)
    @JvmStatic fun bindItemList(
        view: RecyclerView,
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

        (view.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
//        view.itemAnimator?.changeDuration = 0
    }
}
