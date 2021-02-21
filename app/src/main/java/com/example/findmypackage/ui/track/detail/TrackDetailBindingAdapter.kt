package com.example.findmypackage.ui.track.detail;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.data.local.Progress
import kotlin.jvm.JvmStatic;

object TrackDetailBindingAdapter {
    @BindingAdapter(value = ["items", "viewModel"], requireAll = true)
    @JvmStatic fun bindItemList(
        view: RecyclerView,
        items: MutableList<Progress>,
        vm: TrackDetailViewModel
    ) {
        view.adapter?.run {
            if (this is TrackDetailAdapter) {
                this.setItemList(items)
            }
        } ?: run {
            TrackDetailAdapter(vm).apply {
                view.adapter = this
                this.setItemList(items)
            }
        }
    }
}
