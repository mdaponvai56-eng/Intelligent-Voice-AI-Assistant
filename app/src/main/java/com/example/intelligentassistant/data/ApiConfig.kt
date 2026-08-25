package com.example.intelligentassistant.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

data class ApiConfig(
    val apiEndpoint: String = "https://api.openai.com/v1/chat/completions",
    val apiKey: String = "",
    val model: String = "gpt-3.5-turbo"
) {
    companion object {
        private const val PREFS_NAME = "ai_assistant_prefs"
        private const val KEY_CONFIG = "api_config"

        fun save(context: Context, config: ApiConfig) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val json = Gson().toJson(config)
            prefs.edit().putString(KEY_CONFIG, json).apply()
        }

        fun load(context: Context): ApiConfig {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val json = prefs.getString(KEY_CONFIG, null) ?: return ApiConfig()
            return try {
                Gson().fromJson(json, ApiConfig::class.java)
            } catch (e: Exception) {
                ApiConfig()
            }
        }
    }
}
