package com.example.findmypackage.ui.track.add

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.findmypackage.R
import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.databinding.ItemCarrierBinding
import com.example.findmypackage.ui.main.MainAdapter
import com.example.findmypackage.util.log

class TrackAddAdapter(private val viewModel: TrackAddViewModel) : RecyclerView.Adapter<TrackAddAdapter.ItemViewHolder>(){

    private var mDataSet = mutableListOf<Carrier>()
    private var selectedPosition: Int = -1

    companion object {
        const val TYPE_CARRIER = 0

        var c1 = Color.parseColor("#8AAAE5")
        var f1 = Color.parseColor("#FEFEFE")
    }

    fun setItemList(_itemList: MutableList<Carrier>) {
        mDataSet.clear()
        mDataSet.addAll(_itemList)
        notifyDataSetChanged()
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (holder.binding) {
            is ItemCarrierBinding -> {
                holder.binding.vm = viewModel
                holder.binding.item = mDataSet[position]

                holder.binding.tvCarrierName.setOnClickListener {
                    setSelectedPosition(position)
                    it.isSelected = (selectedPosition == position)
                }

                if (selectedPosition == position) {
                    log.e(position)
                    holder.binding.tvCarrierName.setBackgroundColor(f1)
                    holder.binding.tvCarrierName.setTextColor(c1)
                } else {
                    holder.binding.tvCarrierName.setBackgroundColor(c1)
                    holder.binding.tvCarrierName.setTextColor(f1)
                }
            }
        }

    }

    private fun setSelectedPosition(position: Int) {
        val postPosition = selectedPosition
        selectedPosition = if (selectedPosition == position) {
            viewModel.onClickItem(Carrier(-1, "","",""))
            -1
        } else {
            viewModel.onClickItem(mDataSet[position])
            position
        }

        notifyItemChanged(postPosition)
        notifyItemChanged(position)
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