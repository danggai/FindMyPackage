package com.example.findmypackage.data.local

data class Tracks (
    val from: From,
    val to: To,
    val state: State,
    val progresses: List<Progress>,
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
        val text: String,
    )

    data class Progress(
        val time: String,
        val location: Location,
        val status: Status,
        val description: String,
    )

    data class Carrier(
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
