package danggai.app.parcelwhere.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import danggai.app.parcelwhere.BindingFragment
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.databinding.SettingFragmentBinding
import danggai.app.parcelwhere.ui.dialog.RxImageDialog
import danggai.app.parcelwhere.util.EventObserver
import danggai.app.parcelwhere.util.PreferenceManager
import danggai.app.parcelwhere.util.log
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SettingFragment : BindingFragment<SettingFragmentBinding>() {

    companion object {
        val TAG: String = SettingFragment::class.java.simpleName
        fun newInstance() = SettingFragment()
    }

    private lateinit var mVM: SettingViewModel

    @LayoutRes
    override fun getLayoutResId() = R.layout.setting_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm?.let {
            mVM = it
            it.setCommonFun(view)
            it.lvAppVersion.value = getAppVersion()
        }

        initLv()
    }

    override fun onResume() {
        super.onResume()
        initUi()
    }

    private fun initUi() {
        context?.let {
            mVM.lvIsAllowAccessNoti.value = isNotificationPermissionAllowed()
            mVM.lvIsAllowGetNoti.value = PreferenceManager.getBooleanAllowGetNoti(it)
            mVM.lvIsAllowAutoRefresh.value = PreferenceManager.getBooleanAutoRefresh(it)
            mVM.lvAutoRefreshPeriod.value = PreferenceManager.getLongAutoRefreshPeriod(it)
        }

        when(mVM.lvAutoRefreshPeriod.value) {
            15L -> binding.rb15m.isChecked = true
            30L -> binding.rb30m.isChecked = true
            60L -> binding.rb1h.isChecked = true
            120L -> binding.rb2h.isChecked = true
        }
    }

    private fun initLv() {
        mVM.lvStartAccessNotiSetting.observe(viewLifecycleOwner, EventObserver { allowed ->
            log.e()
            activity?.let { act ->
                RxImageDialog(RxImageDialog.Builder(act, R.drawable.help_access_noti_allow, getString(R.string.dialog_allow_noti_help_allow), getString(R.string.confirm), getString(R.string.denied), false))
                    .show()
                    .subscribe {
                        if (it) startAllowNotiPermission()
                        else mVM.lvIsAllowAccessNoti.value = allowed
                    }
            }
        })

        mVM.lvStartGetNotiSetting.observe(viewLifecycleOwner, EventObserver { allowed ->
            log.e()
            activity?.let { act ->
                val msgResource = if (!allowed) R.string.dialog_get_noti_help_allow else R.string.dialog_get_noti_help_reject
                RxImageDialog(RxImageDialog.Builder(act, null, getString(msgResource), getString(R.string.confirm), getString(R.string.denied), false))
                    .show()
                    .subscribe {
                        mVM.lvIsAllowGetNoti.value = if (it) {
                            PreferenceManager.setBoolean(act, Constant.PREF_ALLOW_GET_NOTI, !allowed)
                            !allowed
                        } else allowed
//                        if (it) startAllowNotiPermission()
                    }
            }
        })

        mVM.lvStartNotiFailForm.observe(viewLifecycleOwner, EventObserver { allowed ->
            log.e()
            activity?.let { act ->
                RxImageDialog(RxImageDialog.Builder(act, null, getString(R.string.dialog_start_noti_fail_form), getString(R.string.confirm), getString(R.string.denied), false))
                    .show()
                    .subscribe {
                        if (it) {
                            val itn = Intent(Intent.ACTION_VIEW)
                            itn.data = Uri.parse(Constant.URL_GOOGLE_FORM_NOTI_FAIL)
                            act.startActivity(itn)
                        }
                    }
            }
        })

        mVM.lvSetAutoRefresh.observe(viewLifecycleOwner, EventObserver { allowed ->
            log.e()
            activity?.let { act ->
                PreferenceManager.setBoolean(act, Constant.PREF_AUTO_REFRESH, !allowed)
                mVM.lvIsAllowAutoRefresh.value = !allowed
            }
        })

        mVM.lvSetAutoRefreshPeriod.observe(viewLifecycleOwner, EventObserver { period ->
            activity?.let { act ->
                log.e(period)
                PreferenceManager.setInt(act, Constant.PREF_AUTO_REFRESH_PERIOD, period)
            }
        })
    }
}