package com.example.intelligentassistant.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.intelligentassistant.R
import com.example.intelligentassistant.data.ApiConfig

class SettingsActivity : AppCompatActivity() {

    private lateinit var apiEndpointInput: EditText
    private lateinit var apiKeyInput: EditText
    private lateinit var modelInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        apiEndpointInput = findViewById(R.id.apiEndpointInput)
        apiKeyInput = findViewById(R.id.apiKeyInput)
        modelInput = findViewById(R.id.modelInput)
        saveButton = findViewById(R.id.saveButton)

        loadSettings()
        setupClickListeners()
    }

    private fun loadSettings() {
        val config = ApiConfig.load(this)
        apiEndpointInput.setText(config.apiEndpoint)
        apiKeyInput.setText(config.apiKey)
        modelInput.setText(config.model)
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener { saveSettings() }
    }

    private fun saveSettings() {
        val endpoint = apiEndpointInput.text.toString().trim()
        val apiKey = apiKeyInput.text.toString().trim()
        val model = modelInput.text.toString().trim()

        if (endpoint.isEmpty() || apiKey.isEmpty() || model.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val config = ApiConfig(
            apiEndpoint = endpoint,
            apiKey = apiKey,
            model = model
        )

        ApiConfig.save(this, config)
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
        finish()
    }
}
