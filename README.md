# 🎨 Claudbar Android App

Elegant Android application with glassmorphism UI, Lottie animations, and NVIDIA API integration.

## Features

- **Jetpack Compose UI** - Modern, reactive UI framework
- **Glassmorphism Design** - Transparent, frosted glass effects with blur
- **Lottie Animations** - Cloud mascot animations
- **Chat Functionality** - Powered by NVIDIA Nemotron LLM
- **Bar Live** - Voice input/output with Magpie TTS
- **Search Integration** - Full-text search with results
- **Material Design 3** - Latest design guidelines

## Build & Installation

### GitHub Actions Auto-Build
The APK is automatically compiled on every push to `main` branch via GitHub Actions.

**Download the latest APK:**
1. Go to [GitHub Actions](https://github.com/sciltomia-oss/claudbarcom/actions)
2. Click on the latest workflow run
3. Click "Artifacts"
4. Download `claudbarcom-debug.apk`

### Local Build
```bash
./gradlew clean assembleDebug
# APK at: app/build/outputs/apk/debug/app-debug.apk
```

## Tech Stack

- **Language**: Kotlin + Java
- **UI Framework**: Jetpack Compose
- **Networking**: OkHttp3
- **Animations**: Lottie
- **Async**: Coroutines
- **Build System**: Gradle 8.7
- **Target SDK**: Android 34 (API 34)

## Architecture

```
claudbarcom/
├── .github/workflows/        # GitHub Actions CI/CD
│   └── android-build.yml     # Automated APK builds
├── app/
│   ├── src/main/
│   │   ├── java/com/example/claudbarcom/
│   │   │   ├── MainActivity.java          # Compose entry point
│   │   │   ├── api/ClaudbarApiClient.kt   # HTTP API client
│   │   │   └── ui/home/
│   │   │       ├── HomeScreen.kt          # Main UI
│   │   │       └── ChatViewModel.kt       # Chat logic
│   │   └── res/                           # Resources
│   └── build.gradle.kts                  # Dependencies
└── build.gradle.kts                      # Root build config
```

## API Integration

Connects to Claudbar cloud servers:
- `/auth/login` - User authentication
- `/v1/chat/completions` - Chat with Nemotron AI
- `/v1/tts` - Text-to-speech via Magpie
- `/v1/search` - Web search

##License

Private project

---

**Built with ❤️ by Claude**
