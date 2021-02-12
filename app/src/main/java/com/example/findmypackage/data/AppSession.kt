package com.example.findmypackage.data

import com.example.findmypackage.data.local.Carrier
import com.example.findmypackage.data.res.ResCarrier

object AppSession {

    private var mCarrierList: MutableList<Carrier> = mutableListOf()

    fun setCarrierList (carrierList: ResCarrier) {
        mCarrierList = mutableListOf()
        for ((idx, item) in carrierList.data.withIndex()) {
            mCarrierList.add(Carrier(idx, item.id, item.name, item.tel?:""))
        }
    }

    fun getCarrierList(): List<Carrier> {
        return mCarrierList
    }

}