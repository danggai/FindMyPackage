package com.example.findmypackage.ui.track.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import com.example.findmypackage.BindingActivity
import com.example.findmypackage.R
import com.example.findmypackage.data.db.track.TrackEntity
import com.example.findmypackage.databinding.TrackDetailActivityBinding

class TrackDetailActivity : BindingActivity<TrackDetailActivityBinding>() {

    companion object {
        const val ARG_TRACK_ENTITY = "ARG_TRACK_ENTITY"
        const val ARG_TRACK_ID = "ARG_TRACK_ID"

        fun normalStart (activity: Activity, trackId: String) {
            val intent= Intent(activity, TrackDetailActivity::class.java)
            intent.putExtra(ARG_TRACK_ID, trackId)
            activity.startActivity(intent)
        }

        fun normalStart (activity: Activity, item: TrackEntity) {
            val intent= Intent(activity, TrackDetailActivity::class.java)
            intent.putExtra(ARG_TRACK_ENTITY, item)
            activity.startActivity(intent)
        }
    }

    @LayoutRes
    override fun getLayoutResId() = R.layout.track_detail_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, TrackDetailFragment.newInstance(), TrackDetailFragment.TAG)
            .commit()
    }
}