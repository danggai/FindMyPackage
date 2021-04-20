package danggai.app.parcelwhere.ui.track.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import danggai.app.parcelwhere.BindingActivity
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.databinding.TrackAddActivityBinding

class TrackAddActivity : BindingActivity<TrackAddActivityBinding>() {

    companion object {
        fun normalStart (activity: Activity) {
            val intent= Intent(activity, TrackAddActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @LayoutRes
    override fun getLayoutResId() = R.layout.track_add_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, TrackAddFragment.newInstance(), TrackAddFragment.TAG)
            .commit()
    }
}