package com.example.findmypackage.ui.trackAdd

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.example.findmypackage.BindingFragment
import com.example.findmypackage.R
import com.example.findmypackage.databinding.TrackAddFragmentBinding
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
        }

        initUi()
    }

    private fun initUi() {
        mVM.initUI()
    }
}