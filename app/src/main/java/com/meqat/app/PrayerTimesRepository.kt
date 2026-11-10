package com.meqat.app

import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.Executors

data class PrayerTimes(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)

object PrayerTimesRepository {

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun fetch(
        latitude: Double,
        longitude: Double,
        onSuccess: (PrayerTimes) -> Unit,
        onError: (String) -> Unit
    ) {
        executor.execute {
            var connection: HttpURLConnection? = null

            try {
                val dateFormat = SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.US
                )

                dateFormat.timeZone = TimeZone.getDefault()

                val today = dateFormat.format(Date())

                val address =
                    "https://api.aladhan.com/v1/timings/$today" +
                    "?latitude=$latitude" +
                    "&longitude=$longitude" +
                    "&method=5"

                connection = URL(address)
                    .openConnection() as HttpURLConnection

                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw Exception("تعذر جلب مواقيت الصلاة")
                }

                val response = connection.inputStream
                    .bufferedReader()
                    .use { it.readText() }

                val root = JSONObject(response)

                if (root.optInt("code") != 200) {
                    throw Exception("مصدر المواقيت لم يرجع بيانات صحيحة")
                }

                val timings = root
                    .getJSONObject("data")
                    .getJSONObject("timings")

                val result = PrayerTimes(
                    fajr = cleanTime(timings.getString("Fajr")),
                    sunrise = cleanTime(timings.getString("Sunrise")),
                    dhuhr = cleanTime(timings.getString("Dhuhr")),
                    asr = cleanTime(timings.getString("Asr")),
                    maghrib = cleanTime(timings.getString("Maghrib")),
                    isha = cleanTime(timings.getString("Isha"))
                )

                mainHandler.post {
                    onSuccess(result)
                }

            } catch (e: Exception) {
                mainHandler.post {
                    onError(
                        e.message ?: "حدث خطأ أثناء جلب المواقيت"
                    )
                }
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun cleanTime(value: String): String {
        return value.substringBefore(" ").trim()
    }
}
