package danggai.app.parcelwhere.ui.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import danggai.app.parcelwhere.util.Event
import danggai.app.parcelwhere.util.NonNullMutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel(open val app: Application) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    var lvMakeToast = MutableLiveData<Event<String>>()

    fun getString(resId: Int): String {
        return app.getString(resId)
    }

    fun Disposable.addCompositeDisposable() {
        compositeDisposable.add(this)
    }



}