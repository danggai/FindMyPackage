package com.example.findmypackage.data.res

import com.google.gson.annotations.SerializedName

data class ResTracks(
        @SerializedName("from") val from: From,
        @SerializedName("to") val to: To,
        @SerializedName("state") val state: State,
        @SerializedName("progresses") val progresses: Progresses,
        @SerializedName("carrier") val carrier: ResCarrier,
) {
    data class From(
            val name: String,
            val time: String,
    )

    data class To(
            val name: String,
            val time: String,
    )

    data class State(
            val id: String,
            val text: String,
    )

    data class Progresses(
            val time: String,
            val location: Location,
            val status: Status,
            val description: String,
    )

    data class Carrier (
            val id: String,
            val name: String,
            val tel: String?
    )

    data class Location(
            val name: String,
    )

    data class Status(
            val id: String,
            val text: String,
    )


}