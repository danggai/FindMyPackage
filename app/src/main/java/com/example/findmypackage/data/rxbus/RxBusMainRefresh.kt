package com.example.findmypackage.data.rxbus

import io.reactivex.subjects.PublishSubject

class RxBusMainRefresh{
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