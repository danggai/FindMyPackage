package com.example.findmypackage.data.local

data class Progress(
    val time: String,
    val location: Location,
    val status: Status,
    val description: String,
) {
    data class Location(
        val name: String,
    )

    data class Status(
        val id: String,
        val text: String,
    )
}