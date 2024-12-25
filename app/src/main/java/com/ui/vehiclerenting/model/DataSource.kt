package com.ui.vehiclerenting.model

import android.content.Context
import java.io.IOException

class DataSource {


//    Car Categories

    fun fetchCategories(context: Context): CategoryResponse {
        val jsonString = loadJsonFromAssets(context, "categories.json")
        return parseJson(jsonString ?: "")
    }

//    Car Details

    fun fetchCarDetails(context: Context): CarDetailsResponse {
        val jsonString = loadJsonFromAssets(context, "carDetails.json")
        return parseCarDetailsJson(jsonString ?: "")
    }

    private fun loadJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}