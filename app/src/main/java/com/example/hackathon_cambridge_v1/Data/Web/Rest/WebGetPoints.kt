package com.example.hackathon_cambridge_v1.Data.Web.Rest

import android.util.Log
import com.example.hackathon_cambridge_v1.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WebGetPoints
{
    private val dataURL = URL(BuildConfig.API_URL_POINTS)
    private lateinit var httpURLConnection: HttpURLConnection
    fun getPoints(): String?
    {
        var dataJson: String? = null
        httpURLConnection = dataURL.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        Log.d("MyLog","Before Res ${httpURLConnection.responseCode}")
        if(httpURLConnection.responseCode in 200..299)
        {
            BufferedReader(InputStreamReader(httpURLConnection.inputStream)).use {
                val inputLine = it.readLine()
                dataJson = inputLine
                Log.d("MyLog","RES_POINTSSS: $dataJson")
            }
        }
        if (httpURLConnection.responseCode in 300..600)
        {
            Log.d("MyLog","ERRORCODE: ${httpURLConnection.responseCode}")
        }
        httpURLConnection.disconnect()

        return dataJson
    }
}