package com.example.findmypackage.ui.main

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.example.findmypackage.BindingActivity
import com.example.findmypackage.R
import com.example.findmypackage.databinding.MainActivityBinding

class MainActivity : BindingActivity<MainActivityBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.main_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, MainFragment.newInstance(), MainFragment.TAG)
            .commit()
    }
}