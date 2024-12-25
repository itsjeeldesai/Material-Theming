package com.ui.vehiclerenting.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class CarDetails(
    val imageResource: String,
    val validityText: String,
    val ratingText: String,
    val nameText: String,
    val tripsText: String,
    val locationText: String,
    val priceText: String,
    val type: String,
)

@Serializable
data class CarDetailsResponse(
    val carDetails: List<CarDetails>
)

fun parseCarDetailsJson(jsonString: String): CarDetailsResponse {
    return Json.decodeFromString(jsonString)
}

