package com.example.intelligentassistant.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.intelligentassistant.R

class PermissionsActivity : AppCompatActivity() {

    private lateinit var audioPermButton: Button
    private lateinit var accessibilityPermButton: Button
    private lateinit var screenCaptureButton: Button
    private lateinit var statusTextView: TextView

    private val mediaProjectionManager by lazy {
        getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        audioPermButton = findViewById(R.id.audioPermButton)
        accessibilityPermButton = findViewById(R.id.accessibilityPermButton)
        screenCaptureButton = findViewById(R.id.screenCaptureButton)
        statusTextView = findViewById(R.id.statusTextView)

        setupClickListeners()
        updatePermissionStatus()
    }

    private fun setupClickListeners() {
        audioPermButton.setOnClickListener { requestAudioPermission() }
        accessibilityPermButton.setOnClickListener { requestAccessibilityPermission() }
        screenCaptureButton.setOnClickListener { requestScreenCapturePermission() }
    }

    private fun requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                101
            )
        } else {
            Toast.makeText(this, "Audio permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestAccessibilityPermission() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
        Toast.makeText(
            this,
            "Find and enable 'AI Assistant Accessibility Service'",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun requestScreenCapturePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = mediaProjectionManager.createScreenCaptureIntent()
            startActivity(intent)
        }
    }

    private fun updatePermissionStatus() {
        val audioGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        val status = "Audio: ${if (audioGranted) "✓" else "✗"}"
        statusTextView.text = status
    }
}
