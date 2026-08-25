# Intelligent Voice AI Assistant

A professional Android application that provides an intelligent voice assistant with multi-language support, speech recognition, text-to-speech capabilities, and AI-powered responses.

## Features

✅ **Speech Recognition** - Real-time voice input with multi-language support (Bengali, English, Hindi, Chinese)
✅ **Text-to-Speech** - Natural language audio output
✅ **AI Integration** - Connect to OpenAI or compatible AI APIs
✅ **Accessibility Service** - Screen reading and understanding capabilities
✅ **App Control** - Launch applications via voice commands
✅ **Chat Interface** - Interactive conversation history
✅ **Settings Management** - Configure API endpoints and models
✅ **Permission Management** - Easy permission setup

## Project Structure

```
app/src/main/
├── java/com/example/intelligentassistant/
│   ├── MainActivity.kt                    # Main activity
│   ├── service/
│   │   └── VoiceAssistantService.kt      # Core voice service
│   ├── data/
│   │   ├── ChatMessage.kt                # Message data model
│   │   └── ApiConfig.kt                  # API configuration
│   ├── ui/
│   │   ├── SettingsActivity.kt           # Settings screen
│   │   └── PermissionsActivity.kt        # Permissions screen
│   ├── adapter/
│   │   └── ChatMessageAdapter.kt         # Chat list adapter
│   └── accessibility/
│       └── AssistantAccessibilityService.kt
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── activity_settings.xml
│   │   ├── activity_permissions.xml
│   │   ├── item_user_message.xml
│   │   └── item_assistant_message.xml
│   ├── drawable/
│   │   └── [vector drawables]
│   └── values/
│       ├── colors.xml
│       ├── strings.xml
│       └── themes.xml
├── AndroidManifest.xml
└── ...
```

## Setup Instructions

### Prerequisites
- Android Studio 2022.1 or later
- Android SDK 31 (minimum)
- Gradle 8.0+
- Java 11+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/mdaponvai56-eng/Intelligent-Voice-AI-Assistant.git
   cd Intelligent-Voice-AI-Assistant
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Configure API Settings**
   - Launch the app
   - Tap Settings ⚙️
   - Enter your OpenAI API endpoint and key
   - Select your preferred model (e.g., gpt-3.5-turbo)
   - Tap Save

4. **Grant Permissions**
   - Tap Permissions 🔐
   - Enable Microphone access
   - Enable Accessibility Service (Settings > Accessibility > AI Assistant)

5. **Build and Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

## Usage

### Voice Commands

1. **Launch Apps**
   - "Open YouTube"
   - "Open Chrome"
   - "Launch Gmail"

2. **General Queries**
   - Tap the microphone button 🎤
   - Speak your question
   - Receive AI-powered responses

3. **Language Selection**
   - Choose your preferred language from the dropdown
   - App will switch to that language for speech recognition and text-to-speech

### Settings Configuration

#### API Configuration
- **API Endpoint**: Your AI service endpoint (default: OpenAI)
- **API Key**: Your authentication key
- **Model**: The AI model to use (e.g., gpt-3.5-turbo, gpt-4)

#### Language Support
- Bengali (bn-BD)
- English (en-US)
- Hindi (hi-IN)
- Chinese (zh-CN)

## Dependencies

### Core
- `androidx.appcompat:appcompat` - Android App Compatibility
- `androidx.core:core-ktx` - Kotlin extensions
- `androidx.activity:activity-ktx` - Activity components
- `androidx.fragment:fragment-ktx` - Fragment components

### UI
- `com.google.android.material:material` - Material Design components
- `androidx.constraintlayout:constraintlayout` - ConstraintLayout
- `androidx.recyclerview:recyclerview` - RecyclerView

### Networking
- `com.squareup.okhttp3:okhttp` - HTTP client
- `com.google.code.gson:gson` - JSON serialization

### Accessibility
- `androidx.accessibilityservice:accessibilityservice` - Accessibility API

### Speech
- `androidx.speech:speech` - Speech Recognition

### Other
- `org.jetbrains.kotlinx:kotlinx-coroutines` - Async programming
- `androidx.lifecycle:lifecycle-runtime-ktx` - Lifecycle management

## Architecture

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern:

- **Model**: `ChatMessage`, `ApiConfig` data classes
- **View**: Activities (`MainActivity`, `SettingsActivity`, `PermissionsActivity`) and adapters
- **ViewModel/Service**: `VoiceAssistantService` handles business logic

## Key Components

### MainActivity
- Manages the chat interface
- Handles user input (text and voice)
- Displays conversation history
- Integrates speech recognition and text-to-speech

### VoiceAssistantService
- Processes user messages
- Queries AI APIs
- Detects and handles app launch commands
- Manages API communication

### AssistantAccessibilityService
- Reads screen content
- Provides screen understanding capabilities
- Enables app control

## Permissions Required

- `RECORD_AUDIO` - Microphone access for speech recognition
- `INTERNET` - API communication
- `BIND_ACCESSIBILITY_SERVICE` - Accessibility features
- `READ_EXTERNAL_STORAGE` - File access
- `CAMERA` - Optional camera access

## API Integration

### OpenAI Example

```
Endpoint: https://api.openai.com/v1/chat/completions
Model: gpt-3.5-turbo or gpt-4
```

### Custom AI Service

Modify `VoiceAssistantService.queryAIAPI()` to integrate other AI providers.

## Troubleshooting

### Speech Recognition Not Working
- Ensure microphone permission is granted
- Check internet connection
- Verify language code is correct

### API Errors
- Check API endpoint and key configuration
- Verify API key has required permissions
- Check rate limiting and quota

### Accessibility Service Not Enabling
- Go to Settings > Accessibility
- Find "AI Assistant Accessibility Service"
- Enable the toggle

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License - See LICENSE file for details

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Contact: mdaponvai56@gmail.com

## Roadmap

- [ ] Voice command recording
- [ ] Offline mode support
- [ ] Advanced screen understanding
- [ ] Custom wake words
- [ ] Multi-modal input (vision + voice)
- [ ] Cloud sync for settings
- [ ] Premium features

---

**Made with ❤️ by mdaponvai56-eng**
