package com.example.findmypackage.ui.main;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView
import com.example.findmypackage.data.db.track.TrackEntity
import kotlin.jvm.JvmStatic;

object MainBindingAdapter {
    @BindingAdapter(value = ["items", "viewModel"], requireAll = true)
    @JvmStatic fun bindItemList(
        view: RecyclerView,
        items: MutableList<TrackEntity>,
        vm: MainViewModel
    ) {
        view.adapter?.run {
            if (this is MainAdapter) {
                this.setItemList(items)
            }
        } ?: run {
            MainAdapter(vm).apply {
                view.adapter = this
                this.setItemList(items)
            }
        }
    }
}
