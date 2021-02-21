package com.example.findmypackage.ui.track.detail

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.findmypackage.R
import com.example.findmypackage.data.local.Progress
import com.example.findmypackage.databinding.ItemProgressBinding
import com.example.findmypackage.databinding.ItemProgressEmptyBinding
import java.text.SimpleDateFormat
import java.util.*


class TrackDetailAdapter(private val viewModel: TrackDetailViewModel) : RecyclerView.Adapter<TrackDetailAdapter.ItemViewHolder>() {

    private var mDataSet = mutableListOf<Any>()

    companion object {
        const val TYPE_PROGRESS_ITEM = 0
        const val TYPE_EMPTY = 1

        var f1 = Color.parseColor("#FEFEFE")
        var f2 = Color.parseColor("#F0F0F0")
        var b1 = Color.parseColor("#000000")
        var b2 = Color.parseColor("#777777")

        val beforeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        val afterFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
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

    private fun convertDateString(string: String): String {
        val beforeDate: Date? = beforeFormat.parse(string)
        beforeDate?.let { return afterFormat.format(it) }
        return "날짜 정보가 없습니다."
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
                holder.binding.tvTime.text = convertDateString(item.time)
                when (position) {
                    0 -> {
                        holder.binding.cvBody.setBackgroundColor(f1)
                        holder.binding.tvDescription.setTextColor(b1)
                        holder.binding.tvLocationName.setTextColor(b1)
                        holder.binding.tvStatus.setTextColor(b1)
                        holder.binding.tvTime.setTextColor(b1)
                        holder.binding.lineTop.visibility = View.INVISIBLE
                    }
                    else -> {
                        holder.binding.cvBody.setBackgroundColor(f2)
                        holder.binding.tvDescription.setTextColor(b2)
                        holder.binding.tvLocationName.setTextColor(b2)
                        holder.binding.tvStatus.setTextColor(b2)
                        holder.binding.tvTime.setTextColor(b2)
                        holder.binding.lineTop.visibility = View.VISIBLE
                    }
//                    itemCount-1 -> holder.binding.lineBottom.visibility = View.INVISIBLE
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