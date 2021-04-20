package danggai.app.parcelwhere.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import danggai.app.parcelwhere.BindingActivity
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.databinding.SettingActivityBinding

class SettingActivity : BindingActivity<SettingActivityBinding>() {

    companion object {
        fun normalStart (activity: Activity) {
            val intent= Intent(activity, SettingActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @LayoutRes
    override fun getLayoutResId() = R.layout.setting_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, SettingFragment.newInstance(), SettingFragment.TAG)
            .commit()
    }
}