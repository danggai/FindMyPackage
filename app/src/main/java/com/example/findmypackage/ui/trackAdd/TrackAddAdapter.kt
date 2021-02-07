package com.example.findmypackage.ui.trackAdd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.findmypackage.R
import com.example.findmypackage.data.res.ResCarrier
import com.example.findmypackage.databinding.ItemCarrierBinding


class TrackAddAdapter(val viewModel: TrackAddViewModel) : BaseAdapter() {

    private var mDataSet = arrayListOf<ResCarrier>()

    companion object {
        const val TYPE_CARRIER = 0
    }

    fun addItem(item: ResCarrier) {
        mDataSet.add(item)
        notifyDataSetChanged()
    }

    fun setItemList(_itemList: MutableList<ResCarrier>) {
        mDataSet.clear()
        mDataSet.addAll(_itemList)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataSet.clear()
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mDataSet.size
    }

    override fun getItem(p0: Int): ResCarrier {
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
            holder.binding.tvCarrierName.text = mDataSet[position].name

//            GlideApp.with(parent.context)
//                .load(items[position].img_url)
//                .apply{ placeholder(items[position].img_url_holder) }
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .apply(RequestOptions().fitCenter())
//                .into(holder.binding.ivImage)
        }
        else {
            holder = convertView.tag as ItemViewHolder
        }
        holder.binding.item = mDataSet[position];

        return holder.view
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CARRIER
    }

    class ItemViewHolder internal constructor(binding: ItemCarrierBinding) {
        var view: View = binding.root
        var binding: ItemCarrierBinding = binding
    }
}