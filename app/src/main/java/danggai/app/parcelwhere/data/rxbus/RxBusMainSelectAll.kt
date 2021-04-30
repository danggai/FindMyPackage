package danggai.app.parcelwhere.data.rxbus

import io.reactivex.subjects.PublishSubject

class RxBusMainSelectAll{
    companion object {
        private var busSubject : PublishSubject<Boolean>? = null

        fun getSubject():PublishSubject<Boolean>?{
            if(busSubject == null){
                busSubject = PublishSubject.create()
            }
            return busSubject
        }

        fun release(){
            busSubject = null
        }
    }
}
