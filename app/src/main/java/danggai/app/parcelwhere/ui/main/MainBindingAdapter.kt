package danggai.app.parcelwhere.ui.main;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.data.local.TrackListItem
import danggai.app.parcelwhere.util.log
import kotlin.jvm.JvmStatic;

object MainBindingAdapter {
    @BindingAdapter(value = ["items", "viewModel"], requireAll = true)
    @JvmStatic fun bindItemList(
        view: RecyclerView,
        items: MutableList<TrackListItem>,
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

    @BindingAdapter(value = ["isChanged"], requireAll = true)
    @JvmStatic fun bindRefresh(
        view: RecyclerView,
        isChanged: Boolean,
    ) {
        if (!isChanged) return
        view.adapter?.run {
            if (this is MainAdapter) {
                this.notifyDataSetChanged()
            }
        }
    }

    @BindingAdapter(value = ["isRefreshing", "viewModel"], requireAll = true)
    @JvmStatic fun bindRefresh (
        view: SwipeRefreshLayout,
        isRefreshing: Boolean,
        vm: MainViewModel
    ) {
        view.isRefreshing = isRefreshing
        view.setOnRefreshListener {
            vm.refreshAll()
        }
    }
}