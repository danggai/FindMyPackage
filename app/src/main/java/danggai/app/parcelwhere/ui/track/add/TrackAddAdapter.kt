package danggai.app.parcelwhere.ui.track.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.local.Carrier
import danggai.app.parcelwhere.databinding.ItemCarrierBinding

class TrackAddAdapter(private val viewModel: TrackAddViewModel) : RecyclerView.Adapter<TrackAddAdapter.ItemViewHolder>(){

    private var mDataSet = mutableListOf<Carrier>()
    private var selectedPosition: Int = -1

    companion object {
        const val TYPE_CARRIER = 0
    }

    fun setItemList(_itemList: MutableList<Carrier>) {
        mDataSet.clear()
        mDataSet.addAll(_itemList)
        notifyDataSetChanged()
    }

    override fun getItemId(p0: Int): Long {
        return mDataSet[p0].hashCode().toLong()
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (holder.binding) {
            is ItemCarrierBinding -> {
                holder.binding.vm = viewModel
                holder.binding.item = mDataSet[position]

                holder.binding.tvCarrierName.isSelected = (viewModel.lvCarrierId.value == mDataSet[position].id)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CARRIER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(R.layout.item_carrier, parent)
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    class ItemViewHolder (
        layoutId: Int,
        parent: ViewGroup,
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
    ): RecyclerView.ViewHolder(binding.root)
}