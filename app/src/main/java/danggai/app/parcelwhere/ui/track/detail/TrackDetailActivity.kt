package danggai.app.parcelwhere.ui.track.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import danggai.app.parcelwhere.BindingActivity
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.db.track.TrackEntity
import danggai.app.parcelwhere.databinding.TrackDetailActivityBinding

class TrackDetailActivity : BindingActivity<TrackDetailActivityBinding>() {

    companion object {
        const val ARG_TRACK_ENTITY = "ARG_TRACK_ENTITY"

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