package com.ui.vehiclerenting.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class Category(
    val index: Int,
    val name: String,
)

@Serializable
data class CategoryResponse(
    val categories: List<Category>
)

fun parseJson(jsonString: String): CategoryResponse {
    return Json.decodeFromString(jsonString)
}

