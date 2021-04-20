package danggai.app.parcelwhere.ui.track.detail;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView
import danggai.app.parcelwhere.data.local.Progress
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
