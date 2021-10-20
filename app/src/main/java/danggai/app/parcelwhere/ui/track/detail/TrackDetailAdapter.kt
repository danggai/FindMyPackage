package danggai.app.parcelwhere.ui.track.detail

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.local.Progress
import danggai.app.parcelwhere.databinding.ItemProgressBinding
import danggai.app.parcelwhere.databinding.ItemProgressEmptyBinding
import danggai.app.parcelwhere.util.CommonFunction


class TrackDetailAdapter(private val viewModel: TrackDetailViewModel) : RecyclerView.Adapter<TrackDetailAdapter.ItemViewHolder>() {

    private var mDataSet = mutableListOf<Any>()

    companion object {
        const val TYPE_PROGRESS_ITEM = 0
        const val TYPE_EMPTY = 1

        var f1 = Color.parseColor("#FEFEFE")
        var f2 = Color.parseColor("#F0F0F0")
        var b1 = Color.parseColor("#000000")
        var b2 = Color.parseColor("#777777")
    }

    fun setItemList(_itemList: MutableList<Progress>) {
        mDataSet.clear()

        if (_itemList.size > 0) {
            for (i in _itemList.indices.reversed()) {
                mDataSet.add(_itemList[i])
            }
        } else {
            mDataSet.add(EmptyItem())
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (mDataSet[position]) {
            is Progress -> TYPE_PROGRESS_ITEM
            is EmptyItem -> TYPE_EMPTY
            else -> TYPE_EMPTY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when (viewType) {
            TYPE_PROGRESS_ITEM -> ItemViewHolder(R.layout.item_progress, parent)
            else -> ItemViewHolder(R.layout.item_progress_empty, parent)
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (holder.binding) {
            is ItemProgressBinding -> {
                val item = mDataSet[position] as Progress

                holder.binding.item = item
                holder.binding.vm = viewModel
                holder.binding.tvTime.text = CommonFunction.convertDateString(item.time)
                holder.binding.tvDescription.text = item.description

                if (position == mDataSet.size-1) {
                    holder.binding.cvBody.setBackgroundColor(f2)
                    holder.binding.tvDescription.setTextColor(b2)
                    holder.binding.tvLocationName.setTextColor(b2)
                    holder.binding.tvStatus.setTextColor(b2)
                    holder.binding.tvTime.setTextColor(b2)
                    holder.binding.lineBottom.visibility = View.INVISIBLE
                    holder.binding.vMarker.visibility = View.VISIBLE
                    holder.binding.ivTruck.visibility = View.GONE
                }
                if (position == 0) {
                    holder.binding.cvBody.setBackgroundColor(f1)
                    holder.binding.tvDescription.setTextColor(b1)
                    holder.binding.tvLocationName.setTextColor(b1)
                    holder.binding.tvStatus.setTextColor(b1)
                    holder.binding.tvTime.setTextColor(b1)
                    holder.binding.lineTop.visibility = View.INVISIBLE
                    holder.binding.vMarker.visibility = View.GONE
                    holder.binding.ivTruck.visibility = View.VISIBLE
                }
                if (position != mDataSet.size-1 &&
                        position != 0) {
                    holder.binding.cvBody.setBackgroundColor(f2)
                    holder.binding.tvDescription.setTextColor(b2)
                    holder.binding.tvLocationName.setTextColor(b2)
                    holder.binding.tvStatus.setTextColor(b2)
                    holder.binding.tvTime.setTextColor(b2)
                    holder.binding.vMarker.visibility = View.VISIBLE
                    holder.binding.ivTruck.visibility = View.GONE
                }
            }
            is ItemProgressEmptyBinding -> {
                holder.binding.vm = viewModel
            }
        }
    }

    class ItemViewHolder(
        layoutId: Int,
        parent: ViewGroup,
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
    ) : RecyclerView.ViewHolder(binding.root)

    class EmptyItem()
}