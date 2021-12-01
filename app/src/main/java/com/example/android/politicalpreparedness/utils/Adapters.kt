package com.example.android.politicalpreparedness.utils

import android.annotation.SuppressLint
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class Adapters {

    @SuppressLint("SimpleDateFormat")
    @FromJson
    fun dateFromJson(date: String): Date? {
        return SimpleDateFormat("yyyy-MM-dd").parse(date)
    }

    @SuppressLint("SimpleDateFormat")
    @ToJson
    fun dateToJson(date: Date): String {
        return SimpleDateFormat("yyy-MM-dd").format(date)
    }

}