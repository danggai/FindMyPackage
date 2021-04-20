package danggai.app.parcelwhere.data

import danggai.app.parcelwhere.data.local.Carrier
import danggai.app.parcelwhere.data.res.ResCarrier

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