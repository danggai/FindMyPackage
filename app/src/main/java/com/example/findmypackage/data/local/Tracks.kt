package com.example.findmypackage.data.local

data class Tracks (
    val from: From,
    val to: To,
    val state: State,
    var progresses: List<Progress>,
    val carrier: Carrier,
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
        var text: String,
    )

    data class Carrier(
        val id: String,
        val name: String,
        val tel: String?
    )

}
