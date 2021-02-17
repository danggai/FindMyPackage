package com.example.findmypackage.ui.track.detail

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.example.findmypackage.BindingFragment
import com.example.findmypackage.R
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.databinding.TrackDetailFragmentBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class TrackDetailFragment : BindingFragment<TrackDetailFragmentBinding>() {

    companion object {
        val TAG: String = TrackDetailFragment::class.java.simpleName
        fun newInstance() = TrackDetailFragment()
    }

    private lateinit var mVM: TrackDetailViewModel

    @LayoutRes
    override fun getLayoutResId() = R.layout.track_detail_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm?.let {
            mVM = it
            it.setCommonFun(view)
        }

        initUi()
    }

    private fun initUi() {
        val intent = getIntent()
        intent.getParcelableExtra<TrackEntity>(TrackDetailActivity.ARG_TRACK_ENTITY)?.let {
            mVM.initUi(it)
        }
        intent.getStringExtra(TrackDetailActivity.ARG_CARRIER_ID)?.let { carrierId ->
            intent.getStringExtra(TrackDetailActivity.ARG_TRACK_ID)?.let { trackId ->
                mVM.initUi(carrierId, trackId)
            }
        }
    }
}