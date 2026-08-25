package com.example.intelligentassistant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.intelligentassistant.adapter.ChatMessageAdapter
import com.example.intelligentassistant.data.ChatMessage
import com.example.intelligentassistant.service.VoiceAssistantService
import com.example.intelligentassistant.ui.SettingsActivity
import com.example.intelligentassistant.ui.PermissionsActivity
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var chatListView: ListView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var micButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var permissionsButton: ImageButton
    private lateinit var languageSpinner: Spinner
    private lateinit var clearButton: ImageButton

    private lateinit var adapter: ChatMessageAdapter
    private val chatMessages = mutableListOf<ChatMessage>()

    private lateinit var voiceService: VoiceAssistantService
    private var textToSpeech: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null

    private val languages = listOf("Bengali", "English", "Hindi", "Chinese")
    private var currentLanguage = "Bengali"

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val SETTINGS_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        initializeServices()
        checkPermissions()
        setupLanguageSelector()
        setupClickListeners()
    }

    private fun initializeViews() {
        chatListView = findViewById(R.id.chatListView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        micButton = findViewById(R.id.micButton)
        stopButton = findViewById(R.id.stopButton)
        settingsButton = findViewById(R.id.settingsButton)
        permissionsButton = findViewById(R.id.permissionsButton)
        languageSpinner = findViewById(R.id.languageSpinner)
        clearButton = findViewById(R.id.clearButton)

        adapter = ChatMessageAdapter(this, chatMessages)
        chatListView.adapter = adapter
    }

    private fun initializeServices() {
        voiceService = VoiceAssistantService(this)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                setTextToSpeechLanguage()
            } else {
                showMessage("Assistant", getString(R.string.error_tts_init))
            }
        }
    }

    private fun setTextToSpeechLanguage() {
        val locale = when (currentLanguage) {
            "Bengali" -> Locale("bn", "BD")
            "English" -> Locale.ENGLISH
            "Hindi" -> Locale("hi", "IN")
            "Chinese" -> Locale.CHINESE
            else -> Locale.ENGLISH
        }
        textToSpeech?.language = locale
    }

    private fun setupLanguageSelector() {
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = spinnerAdapter
        languageSpinner.setSelection(0)
    }

    private fun setupClickListeners() {
        sendButton.setOnClickListener { sendMessage() }
        micButton.setOnClickListener { startListening() }
        stopButton.setOnClickListener { stopListening() }
        clearButton.setOnClickListener { clearChat() }
        settingsButton.setOnClickListener { openSettings() }
        permissionsButton.setOnClickListener { openPermissions() }

        languageSpinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                currentLanguage = languages[position]
                setTextToSpeechLanguage()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        })
    }

    private fun sendMessage() {
        val message = messageInput.text.toString().trim()
        if (message.isNotEmpty()) {
            addMessageToChat(message, true)
            messageInput.text.clear()
            processUserMessage(message)
        }
    }

    private fun startListening() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, getLanguageCode())
            putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.listening))
        }

        try {
            speechRecognizer?.startListening(intent)
            addMessageToChat(getString(R.string.listening), false)
        } catch (e: Exception) {
            showMessage("Assistant", getString(R.string.error_stt_init))
        }
    }

    private fun stopListening() {
        speechRecognizer?.stopListening()
    }

    private fun getLanguageCode(): String = when (currentLanguage) {
        "Bengali" -> "bn-BD"
        "English" -> "en-US"
        "Hindi" -> "hi-IN"
        "Chinese" -> "zh-CN"
        else -> "en-US"
    }

    private fun processUserMessage(message: String) {
        lifecycleScope.launch {
            val response = voiceService.getAIResponse(message)
            addMessageToChat(response, false)
            speakResponse(response)
        }
    }

    private fun addMessageToChat(message: String, isUser: Boolean) {
        val chatMessage = ChatMessage(message, isUser)
        chatMessages.add(chatMessage)
        adapter.notifyDataSetChanged()
        chatListView.setSelection(adapter.count - 1)
    }

    private fun speakResponse(text: String) {
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    private fun clearChat() {
        chatMessages.clear()
        adapter.notifyDataSetChanged()
    }

    private fun openSettings() {
        startActivityForResult(Intent(this, SettingsActivity::class.java), SETTINGS_REQUEST_CODE)
    }

    private fun openPermissions() {
        startActivity(Intent(this, PermissionsActivity::class.java))
    }

    private fun showMessage(sender: String, message: String) {
        addMessageToChat("$sender: $message", false)
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech?.shutdown()
        speechRecognizer?.destroy()
    }
}
