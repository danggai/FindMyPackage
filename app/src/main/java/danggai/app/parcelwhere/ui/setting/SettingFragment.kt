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
import danggai.app.parcelwhere.util.CommonFunction
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
        setStatusBarColorWhite()
        initUi()
    }

    private fun initUi() {
        context?.let {
            mVM.lvIsAllowNotiPermission.value = isNotificationPermissionAllowed()
            mVM.lvIsAllowNotiWhenAutoRegister.value = PreferenceManager.getBooleanNotiWhenAutoRegister(it)
            mVM.lvIsAllowAutoRefresh.value = PreferenceManager.getBooleanAutoRefresh(it)
            mVM.lvIsAllowNotiWhenParcelRefresh.value = PreferenceManager.getBooleanNotiWhenParcelRefresh(it)
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
        mVM.lvSetNotiPermission.observe(viewLifecycleOwner, EventObserver { allowed ->
            log.e(allowed)
            activity?.let { act ->
                RxImageDialog(RxImageDialog.Builder(act, R.drawable.help_noti_permission, getString(R.string.dialog_noti_permission_allow), getString(R.string.confirm), getString(R.string.denied), false))
                    .show()
                    .subscribe {
                        if (it) startAllowNotiPermission()
                        else mVM.lvIsAllowNotiPermission.value = allowed
                    }
            }
        })

        mVM.lvSetNotiWhenAutoRegister.observe(viewLifecycleOwner, EventObserver { allowed ->
            log.e()
            activity?.let { act ->
                val msgResource = if (!allowed) R.string.dialog_noti_when_auto_register_allow else R.string.dialog_noti_when_auto_register_reject
                RxImageDialog(RxImageDialog.Builder(act, null, getString(msgResource), getString(R.string.confirm), getString(R.string.denied), false))
                    .show()
                    .subscribe {
                        mVM.lvIsAllowNotiWhenAutoRegister.value = if (it) {
                            PreferenceManager.setBooleanNotiWhenAutoRegister(act, !allowed)
                            !allowed
                        } else allowed
//                        if (it) startAllowNotiPermission()
                    }
            }
        })

        mVM.lvActionNotiFailForm.observe(viewLifecycleOwner, EventObserver { allowed ->
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
                PreferenceManager.setBooleanAutoRefresh(act, !allowed)
                mVM.lvIsAllowAutoRefresh.value = !allowed
                if (!allowed) {
                    CommonFunction.startUniquePeriodicRefreshWorker(act)
                } else {
                    CommonFunction.cancellAllWorkers(act)
                }
            }
        })

        mVM.lvSetNotiWhenParcelRefresh.observe(viewLifecycleOwner, EventObserver { allowed ->
            log.e()
            activity?.let { act ->
                val msgResource = if (!allowed) R.string.dialog_noti_when_parcel_refresh_allow else R.string.dialog_noti_when_parcel_refresh_reject
                RxImageDialog(RxImageDialog.Builder(act, null, getString(msgResource), getString(R.string.confirm), getString(R.string.denied), false))
                    .show()
                    .subscribe {
                        mVM.lvIsAllowNotiWhenParcelRefresh.value = if (it) {
                            PreferenceManager.setBooleanNotiWhenParcelRefresh(act, !allowed)
                            !allowed
                        } else allowed
//                        if (it) startAllowNotiPermission()
                    }
            }
        })

        mVM.lvSetAutoRefreshPeriod.observe(viewLifecycleOwner, EventObserver { period ->
            activity?.let { act ->
                log.e(period)
                PreferenceManager.setLongAutoRefreshPeriod(act, period)
                CommonFunction.startUniquePeriodicRefreshWorker(act, period)
            }
        })
    }
}