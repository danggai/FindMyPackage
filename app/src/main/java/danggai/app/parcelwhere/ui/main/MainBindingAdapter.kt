package danggai.app.parcelwhere.ui.main;

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import danggai.app.parcelwhere.data.local.TrackListItem
import danggai.app.parcelwhere.ui.track.add.TrackAddAdapter

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
                this.setHasStableIds(true)
                view.adapter = this
                this.setItemList(items)
            }
        }
    }

    @BindingAdapter(value = ["isChanged"], requireAll = true)
    @JvmStatic fun bindNotifyDataSetChanged(
        view: RecyclerView,
        isChanged: Boolean,
    ) {
        if (!isChanged) return
        view.adapter?.run {
            if (this is MainAdapter) {
                this.notifyDataSetChanged()
            }
            else if (this is TrackAddAdapter) {
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