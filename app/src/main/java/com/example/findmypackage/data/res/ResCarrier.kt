package com.example.findmypackage.data.res

import com.google.gson.annotations.SerializedName

data class ResCarrier (
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("tel") val tel: String?) {

}