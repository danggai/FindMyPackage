package com.example.findmypackage.ui.track.add

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import com.example.findmypackage.BindingFragment
import com.example.findmypackage.R
import com.example.findmypackage.databinding.TrackAddFragmentBinding
import com.example.findmypackage.ui.track.detail.TrackDetailActivity
import com.example.findmypackage.util.log
import org.koin.androidx.viewmodel.ext.android.getViewModel

class TrackAddFragment : BindingFragment<TrackAddFragmentBinding>() {

    companion object {
        val TAG: String = TrackAddFragment::class.java.simpleName
        fun newInstance() = TrackAddFragment()
    }

    private lateinit var mVM: TrackAddViewModel

    @LayoutRes
    override fun getLayoutResId() = R.layout.track_add_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm?.let {
            mVM = it
            it.setCommonFun(view)
        }

        initLv()
    }

    private fun initLv() {
        mVM.lvStartDetailAct.observe(viewLifecycleOwner, Observer {
            if (it) {
                log.e()
                activity?.let { act ->
                    TrackDetailActivity.normalStart(act, mVM.lvTrackId.value!! )
                    act.finish()
                }
            }
        })
    }
}