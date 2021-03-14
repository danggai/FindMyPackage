package com.example.findmypackage.util

object CarrierUtil {


    private val carrierMap = linkedMapOf(
        "DHL" to "de.dhl",
        "Sagawa" to "jp.sagawa",
        "Kuroneko Yamato" to "jp.yamato",
        "Japan Post" to "jp.yuubin",
        "CJ대한통운" to "kr.cjlogistics",
        "CU 편의점택배" to "kr.cupost",
        "GS Postbox택배" to "kr.cvsnet",
        "CWAY" to "kr.cway",
        "대신택배" to "kr.daesin",
        "우체국택배" to "kr.epost",
        "한의사랑택배" to "kr.hanips",
        "한진택배" to "kr.hanjin",
        "합동택배" to "kr.hdexp",
        "홈픽" to "kr.homepick",
        "한서호남택배" to "kr.honamlogis",
        "일양로지스" to "kr.ilyanglogis",
        "경동택배" to "kr.kdexp",
        "건영택배" to "kr.kunyoung",
        "로젠택배" to "kr.logen",
        "롯데택배" to "kr.lotte",
        "SLX" to "kr.slx",
        "성원글로벌카고" to "kr.swgexp",
        "TNT" to "nl.tnt",
        "EMS" to "un.upu.ems",
        "Fedex" to "us.fedex",
        "UPS" to "us.ups",
        "USPS" to "us.usps",
        
        "대한통운" to "kr.cjlogistics",
        "CUPost" to "kr.cupost",
        "Postbox" to "kr.cvsnet",
        "우체국" to "kr.epost",
    )

    fun getCarrierId(string: String): String {
        for (item in carrierMap) {
            if (string.contains(item.key)) return item.value
        }
        return ""
    }

    fun getCarrierName(string: String): String {
        for (item in carrierMap) {
            if (string.contains(item.value)) return item.key
        }
        return ""
    }

    fun checkCarrierId(string: String): Boolean {
        for (item in carrierMap) {
            if (string == item.value) return true
        }
        return false
    }


}