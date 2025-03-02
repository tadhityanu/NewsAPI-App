package com.example.alfagifttest.Util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtil {

    fun formatDate(inputDate: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Set UTC for correct parsing

        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        val date: Date = inputDate?.let { inputFormat.parse(it) } ?: return "" // Parse input date
        return outputFormat.format(date) // Format into desired output
    }

}