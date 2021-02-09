package com.example.findmypackage.ui.trackAdd

import android.graphics.Color
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.findmypackage.R
import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.databinding.ItemCarrierBinding
import com.example.findmypackage.util.log

class TrackAddAdapter(private val viewModel: TrackAddViewModel) : BaseAdapter() {

    private var mDataSet = mutableListOf<Carrier>()
    private var selectedPosition: Int = -1

    companion object {
        const val TYPE_CARRIER = 0

        var c1 = Color.rgb(187, 134, 252)
        var f1 = Color.rgb(255, 255, 255)
    }

    fun setItemList(_itemList: MutableList<Carrier>) {
        mDataSet.clear()
        mDataSet.addAll(_itemList)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mDataSet.size
    }

    override fun getItem(p0: Int): Carrier {
        return mDataSet[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var holder: ItemViewHolder

        if (convertView == null) {
            val itemBinding: ItemCarrierBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_carrier, parent, false)
            holder = ItemViewHolder(itemBinding)

            holder.view = itemBinding.root
            holder.view.tag = holder
            holder.binding.vm = viewModel

            holder.view.setOnClickListener {
                setSelectedPosition(position)
            }
        }
        else {
            holder = convertView.tag as ItemViewHolder
        }

        if (selectedPosition == position) {
            log.e(position)
            holder.binding.tvCarrierName.setBackgroundColor(f1)
            holder.binding.tvCarrierName.setTextColor(c1)
        } else {
            holder.binding.tvCarrierName.setBackgroundColor(c1)
            holder.binding.tvCarrierName.setTextColor(f1)
        }

        holder.binding.item = mDataSet[position]
        holder.view.isSelected = (selectedPosition == position)

        return holder.view
    }

    private fun setSelectedPosition(position: Int) {
        selectedPosition = if (selectedPosition == position) {
            viewModel.onClickItem(Carrier(-1, "","",""))
            -1
        } else {
            viewModel.onClickItem(mDataSet[position])
            position
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CARRIER
    }

    class ItemViewHolder internal constructor(binding: ItemCarrierBinding) {
        var view: View = binding.root
        var binding: ItemCarrierBinding = binding
    }
}