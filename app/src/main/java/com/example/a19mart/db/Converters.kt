package com.example.a19mart.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(nodeList: List<Node>): String {
        val gson = Gson()
        return gson.toJson(nodeList)
    }

    @TypeConverter
    fun toList(nodeListString: String): List<Node> {
        val gson = Gson()
        val type = object : TypeToken<List<Node>>() {}.type
        return gson.fromJson(nodeListString, type)
    }
}