package danggai.app.parcelwhere.ui.main

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle
import danggai.app.parcelwhere.BindingFragment
import danggai.app.parcelwhere.Constant
import danggai.app.parcelwhere.R
import danggai.app.parcelwhere.data.rxbus.RxBusMainSelectAll
import danggai.app.parcelwhere.databinding.MainFragmentBinding
import danggai.app.parcelwhere.ui.dialog.RxImageDialog
import danggai.app.parcelwhere.ui.setting.SettingActivity
import danggai.app.parcelwhere.ui.track.add.TrackAddActivity
import danggai.app.parcelwhere.ui.track.detail.TrackDetailActivity
import danggai.app.parcelwhere.util.CommonFunction
import danggai.app.parcelwhere.util.EventObserver
import danggai.app.parcelwhere.util.PreferenceManager
import danggai.app.parcelwhere.util.log
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainFragment : BindingFragment<MainFragmentBinding>() {

    companion object {
        val TAG: String = MainFragment::class.java.simpleName
        fun newInstance() = MainFragment()
    }

    private lateinit var mVM: MainViewModel
    private lateinit var clipboard: ClipboardManager
    private var spotlight: Spotlight? = null

    @LayoutRes
    override fun getLayoutResId() = R.layout.main_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        RxBusMainSelectAll.getSubject()?.let { event ->
            event.subscribe {
                log.e()
                if (it) mVM.getAllTrackList()
            }
        }

        context?.let {
            if (PreferenceManager.getBooleanIsFirstRun(it)) {
                log.e()
                activity?.let { act ->
                    RxImageDialog(RxImageDialog.Builder(act, R.drawable.help_noti_permission, getString(R.string.dialog_noti_permission_allow_first), getString(R.string.confirm), getString(R.string.denied), false))
                        .show()
                        .subscribe { confirm ->
                            if (confirm) startAllowNotiPermission()
                            PreferenceManager.setBooleanIsFirstRun(act, false)
                        }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        context?.let { act ->
            CommonFunction.startUniquePeriodicRefreshWorker(act)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm?.let {
            mVM = it
            it.setCommonFun(view)
        }

        initUi()
        initLv()
        initClipBoard()
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBusMainSelectAll.release()
    }

    private fun initUi() {
        mVM.initUI()
    }

    private fun initClipBoard() {
        context?.let { cont ->
            clipboard = ContextCompat.getSystemService(cont, ClipboardManager::class.java) as ClipboardManager
            clipboard.addPrimaryClipChangedListener {
                if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true) {
                    //                        makeToast(String.format(getString(R.string.msg_copy_complete), trackId))
                    log.e()
                    activity?.let { act ->
                        makeToast(act, getString(R.string.msg_copy_complete_temp))
                    }
                }
            }
        }
    }

    private fun initLv() {
        mVM.lvStartAddAct.observe(viewLifecycleOwner, EventObserver{
            if (it) {
                log.d()
                spotlight?.finish()
                activity?.let {act -> TrackAddActivity.normalStart(act)}
            }
        })
        mVM.lvStartSettingAct.observe(viewLifecycleOwner, EventObserver{
            if (it) {
                log.d()
                spotlight?.finish()
                activity?.let {act -> SettingActivity.normalStart(act)}
            }
        })
        mVM.lvStartDetailAct.observe(viewLifecycleOwner, EventObserver{ item ->
            log.d()
            spotlight?.finish()
            activity?.let {act -> TrackDetailActivity.normalStart(act, item)}
        })
        mVM.lvCopyClipboard.observe(viewLifecycleOwner, EventObserver { trackId ->
            log.e()
            activity?.let {
                val clip = ClipData.newPlainText(Constant.CLIPBOARD_LABEL_TRACK_ID, trackId)
                clipboard.setPrimaryClip(clip)
            }
        })
        mVM.lvStartTutorial.observe(viewLifecycleOwner, EventObserver{
            log.d()
            startTutorial()
        })
    }

    private fun startTutorial() {
        view?.let { view ->
            val targets: MutableList<com.takusemba.spotlight.Target> = ArrayList()

            // first target
            val first = layoutInflater.inflate(R.layout.layout_target, FrameLayout(requireContext()))
            first.findViewById<TextView>(R.id.tv_text).text = getString(R.string.tutorial_msg_1)
            first.findViewById<TextView>(R.id.tv_page).text = "1/3"
            val firstTarget = com.takusemba.spotlight.Target.Builder()
                .setAnchor(view.findViewById<View>(R.id.btn_add))
                .setShape(Circle(100f))
                .setOverlay(first)
                .build()

            targets.add(firstTarget)

            // second target
            val second = layoutInflater.inflate(R.layout.layout_target, FrameLayout(requireContext()))
            second.findViewById<TextView>(R.id.tv_text).text = getString(R.string.tutorial_msg_2)
            second.findViewById<TextView>(R.id.tv_page).text = "2/3"
            val secondTarget = com.takusemba.spotlight.Target.Builder()
                .setAnchor(view.findViewById<View>(R.id.btn_setting))
                .setShape(Circle(75f))
                .setOverlay(second)
                .build()

            targets.add(secondTarget)

            // third target
            val third = layoutInflater.inflate(R.layout.layout_target, FrameLayout(requireContext()))
            third.findViewById<TextView>(R.id.tv_text).text = getString(R.string.tutorial_msg_3)
            third.findViewById<TextView>(R.id.tv_page).text = "3/3"
            third.findViewById<TextView>(R.id.btn_next).text = "닫기"
            val thirdTarget = com.takusemba.spotlight.Target.Builder()
                .setAnchor(view.findViewById<View>(R.id.tutorial_target))
                .setShape(RoundedRectangle(700f, 1000f, 50f))
                .setOverlay(third)
                .build()

            targets.add(thirdTarget)

            // create spotlight
            spotlight = Spotlight.Builder(requireActivity())
                .setTargets(targets)
                .setBackgroundColorRes(R.color.b5)
                .setDuration(100L)
                .setAnimation(DecelerateInterpolator(10f))
                .build()

            spotlight?.start()

            val nextTarget = View.OnClickListener {
                log.e()
                spotlight?.next()
            }

            first.findViewById<View>(R.id.btn_next).setOnClickListener(nextTarget)
            second.findViewById<View>(R.id.btn_next).setOnClickListener(nextTarget)
            third.findViewById<View>(R.id.btn_next).setOnClickListener(nextTarget)
        }
    }
}
