package com.example.findmypackage.ui.main

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.example.findmypackage.BindingFragment
import com.example.findmypackage.R
import com.example.findmypackage.databinding.MainFragmentBinding
import com.example.findmypackage.ui.track.add.TrackAddActivity
import com.example.findmypackage.ui.track.detail.TrackDetailActivity
import com.example.findmypackage.util.log
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainFragment : BindingFragment<MainFragmentBinding>() {

    companion object {
        val TAG: String = MainFragment::class.java.simpleName
        fun newInstance() = MainFragment()
    }

    private lateinit var mVM: MainViewModel

    @LayoutRes
    override fun getLayoutResId() = R.layout.main_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm?.let {
            mVM = it
            it.setCommonFun(view)
        }

        initUi()
        initLv()
    }

    override fun onResume() {
        super.onResume()
        log.e()
        mVM.getAllTrackList()
    }

    private fun initUi() {
        mVM.initUI()
    }

    private fun initLv() {
        mVM.lvStartAddAct.observe(viewLifecycleOwner, Observer{
            if (it) {
                log.d()
                activity?.let {act -> TrackAddActivity.normalStart(act)}
            }
        })
        mVM.lvStartDetailAct.observe(viewLifecycleOwner, Observer{ item ->
            log.d()
            activity?.let {act -> TrackDetailActivity.normalStart(act, item)}
        })
    }
}