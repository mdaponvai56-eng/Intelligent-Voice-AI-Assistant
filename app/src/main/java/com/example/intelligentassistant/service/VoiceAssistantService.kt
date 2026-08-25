package com.example.intelligentassistant.service

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.example.intelligentassistant.data.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class VoiceAssistantService(private val context: Context) {

    private val httpClient = OkHttpClient()
    private val gson = Gson()
    private val appIntents = mapOf(
        "youtube" to "com.google.android.youtube",
        "facebook" to "com.facebook.katana",
        "chrome" to "com.android.chrome",
        "camera" to "com.android.camera",
        "gallery" to "com.android.gallery3d",
        "settings" to "com.android.settings",
        "gmail" to "com.google.android.gm",
        "maps" to "com.google.android.apps.maps",
        "messages" to "com.android.messaging",
        "contacts" to "com.android.contacts"
    )

    suspend fun getAIResponse(userMessage: String): String = withContext(Dispatchers.IO) {
        // Check if it's an app launch command
        val appCommand = detectAppCommand(userMessage)
        if (appCommand.isNotEmpty()) {
            return@withContext launchApp(appCommand)
        }

        // Otherwise, get AI response from API
        return@withContext queryAIAPI(userMessage)
    }

    private fun detectAppCommand(message: String): String {
        val lowerMessage = message.lowercase()
        for ((app, _) in appIntents) {
            if (lowerMessage.contains(app)) {
                return app
            }
        }
        return ""
    }

    private fun launchApp(appName: String): String {
        val packageName = appIntents[appName] ?: return "App not found"
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                context.startActivity(intent)
                "Opening $appName"
            } else {
                "$appName is not installed"
            }
        } catch (e: Exception) {
            "Failed to open $appName: ${e.message}"
        }
    }

    private fun queryAIAPI(userMessage: String): String {
        return try {
            val config = ApiConfig.load(context)
            if (config.apiKey.isEmpty()) {
                return "Please configure your API settings first"
            }

            val requestBody = gson.toJson(
                mapOf(
                    "messages" to listOf(
                        mapOf("role" to "user", "content" to userMessage)
                    ),
                    "model" to config.model
                )
            ).toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url(config.apiEndpoint)
                .post(requestBody)
                .addHeader("Authorization", "Bearer ${config.apiKey}")
                .addHeader("Content-Type", "application/json")
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string() ?: "No response"
            } else {
                "API Error: ${response.code}"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}
