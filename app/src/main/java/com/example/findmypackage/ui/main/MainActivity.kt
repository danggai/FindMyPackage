package com.example.findmypackage.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.app.NotificationManagerCompat
import com.example.findmypackage.BindingActivity
import com.example.findmypackage.R
import com.example.findmypackage.databinding.MainActivityBinding

class MainActivity : BindingActivity<MainActivityBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.main_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        if (!isNotificationPermissionAllowed())
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));

        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, MainFragment.newInstance(), MainFragment.TAG)
            .commit()
    }

    private fun isNotificationPermissionAllowed(): Boolean {
        return NotificationManagerCompat.getEnabledListenerPackages(applicationContext)
            .any { enabledPackageName ->
                enabledPackageName == packageName
            }
    }

}