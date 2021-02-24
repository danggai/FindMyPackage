package com.example.findmypackage.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.findmypackage.Constant
import com.example.findmypackage.R
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.databinding.ItemTrackBinding
import com.example.findmypackage.databinding.ItemTrackEmptyBinding
import com.example.findmypackage.ui.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainAdapter(private val viewModel: MainViewModel) : RecyclerView.Adapter<MainAdapter.ItemViewHolder>() {

    private var mDataSet = mutableListOf<Any>()

    companion object {
        const val TYPE_TRACK_ITEM = 0
        const val TYPE_EMPTY = 1
    }

    fun setItemList(_itemList: MutableList<TrackEntity>) {
        mDataSet.clear()
        if (_itemList.size > 0) {
            mDataSet.addAll(_itemList)
        } else {
            mDataSet.add(EmptyItem())
        }
        notifyDataSetChanged()
    }

    private fun convertDateString(string: String): String {
        val beforeDate: Date? = SimpleDateFormat(Constant.DATE_FORMAT_BEFORE).parse(string)
        beforeDate?.let { return SimpleDateFormat(Constant.DATE_FORMAT_AFTER).format(it) }
        return "날짜 정보가 없습니다."
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return when (mDataSet[position]) {
            is TrackEntity -> TYPE_TRACK_ITEM
            is EmptyItem -> TYPE_EMPTY
            else -> TYPE_EMPTY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when (viewType) {
            TYPE_TRACK_ITEM -> ItemViewHolder(R.layout.item_track, parent)
            else -> ItemViewHolder(R.layout.item_track_empty, parent)
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (holder.binding) {
            is ItemTrackBinding -> {
                val item = mDataSet[position] as TrackEntity
                holder.binding.item = item
                holder.binding.vm = viewModel
                holder.binding.tvTime.text = item.recentTime?.let {convertDateString(item.recentTime)}
            }
            is ItemTrackEmptyBinding -> {
                holder.binding.vm = viewModel
            }
        }
    }

    class ItemViewHolder (
        layoutId: Int,
        parent: ViewGroup,
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
    ):RecyclerView.ViewHolder(binding.root)

    class EmptyItem()
}