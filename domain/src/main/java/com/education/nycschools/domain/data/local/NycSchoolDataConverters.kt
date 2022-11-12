package com.education.nycschools.domain.data.local

import androidx.room.TypeConverter

class NycSchoolDataConverters {
    @TypeConverter
    fun listToString(values: List<Int>): String {
        val strList = mutableListOf<String>()
        values.forEach {
            strList.add(it.toString())
        }
        return strList.joinToString(",")
    }

    @TypeConverter
    fun stringToList(value: String): List<Int> {
        val intList = mutableListOf<Int>()
        value.split(",").forEach {
            intList.add(it.toInt())
        }
        return intList
    }
}