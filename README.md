# InterDate 📱

A modern Android dating application built with cutting-edge technologies and best practices.

## 📋 Overview

InterDate is a location-based dating app that connects users in real-time. The app provides a seamless, intuitive user experience with features designed to help users find meaningful connections based on proximity and shared interests.

**Version:** 2026.01.12  
**Min SDK:** Android 7.0 (API 24)  
**Target SDK:** Android 15 (API 35)  
**Build Tools:** 35.0.0

## 🎯 Features

- **Location-Based Matching** - Find users near you using GPS technology
- **Real-Time Database Sync** - Instant updates with Firebase Realtime Database
- **User Authentication** - Secure sign-up and login with Firebase Authentication
- **Image Management** - Upload and manage profile photos with Firebase Storage
- **Push Notifications** - Stay connected with Firebase Cloud Messaging
- **Crash Analytics** - Monitor app stability with Firebase Crashlytics
- **In-App Messaging** - Receive important updates and promotions
- **Ad Integration** - Google Play Services for ads support
- **reCAPTCHA Protection** - Protect against bot attacks and spam

## 🛠️ Technology Stack

### Android Framework & Libraries

| Technology | Purpose |
|-----------|---------|
| **Kotlin** | Primary programming language |
| **Android X (AppCompat)** | Modern Android components and backward compatibility |
| **Material Design** | Beautiful, consistent UI components |
| **ConstraintLayout** | Flexible responsive layouts |
| **Android Navigation** | Fragment-based navigation with type-safe arguments |
| **View Binding** | Type-safe view access without reflection |
| **Core Splashscreen** | Modern splash screen implementation |

### Firebase Services

The app leverages Google's Firebase platform for backend services:

| Service | Usage |
|---------|-------|
| **Firebase Authentication** | User login, registration, and session management |
| **Firebase Realtime Database** | Real-time data synchronization and messaging |
| **Firebase Storage** | Store and retrieve user profile images and media |
| **Firebase Cloud Messaging (FCM)** | Push notifications to users |
| **Firebase Analytics** | Track user behavior and app metrics |
| **Firebase Crashlytics** | Monitor and report app crashes |
| **Firebase In-App Messaging** | Display targeted messages within the app |

### Networking & HTTP

| Technology | Purpose |
|-----------|---------|
| **OkHttp** | HTTP client with connection pooling |
| **Logging Interceptor** | Debug network requests and responses |

### Image & Media

| Technology | Purpose |
|-----------|---------|
| **Glide** | Fast, efficient image loading and caching |
| **Shimmer** | Skeleton loading animations for better UX |
| **PhotoView** | Interactive image viewer with pinch-to-zoom |

### Location & Maps

| Technology | Purpose |
|-----------|---------|
| **Google Play Services - Location** | Precise location tracking and geofencing |
| **Google Play Services - Auth** | Secure authentication methods |
| **Google Play Services - Ads** | Ad integration and monetization |

### Architecture & Dependencies

| Technology | Purpose |
|-----------|---------|
| **Jetpack Lifecycle** | Manage component lifecycle states |
| **WorkManager** | Schedule background tasks reliably |
| **Activity KTX** | Kotlin extensions for Activities |
| **Core KTX** | Kotlin extensions for common Android operations |
| **RecaptchaV2** | Bot protection and spam prevention |

### Build & Compilation

| Tool | Purpose |
|------|---------|
| **Gradle KTS** | Type-safe build configuration |
| **Core Library Desugaring** | Use modern Java APIs on older Android versions |
| **NDK 28.0.12433566** | Native code support if needed |
| **Java 11** | Modern Java language features |

## 📦 Project Structure

```
InterDate/
├── app/                           # Main application module
│   ├── src/
│   │   ├── main/                  # Source code
│   │   ├── androidTest/           # UI and integration tests
│   │   └── test/                  # Unit tests
│   ├── build.gradle.kts           # App-level build configuration
│   └── google-services.json       # Firebase configuration
├── gradle/
│   └── libs.versions.toml         # Centralized dependency versions
├── build.gradle.kts               # Root-level build configuration
├── settings.gradle.kts            # Project settings
└── gradle.properties              # Gradle properties

```

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest version)
- Android SDK 35 or higher
- Java 11 or higher
- Gradle 8.0 or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ThabeloDeveloper/InterDate.git
   cd InterDate
   ```

2. Open the project in Android Studio:
   - File → Open → Select the InterDate directory

3. Configure Firebase:
   - Replace `app/google-services.json` with your Firebase project's configuration file
   - Download it from Firebase Console

4. Set up environment variables (for the JFrog repository):
   ```bash
   export paypal_sgerritz=your_username
   export JFROG_API_KEY=your_api_key
   ```

5. Build and run:
   ```bash
   ./gradlew build
   ```

   Or in Android Studio: Build → Make Project

## 🔐 Security Features

- **Firebase Authentication** - Secure user authentication with multiple sign-in methods
- **reCAPTCHA Integration** - Bot protection on sensitive endpoints
- **Network Security** - TLS/SSL encryption for all network requests
- **ProGuard Obfuscation** - Code obfuscation in release builds
- **Firebase Security Rules** - Database and storage access control

## 🧪 Testing

The project includes test configurations:

```bash
# Run all tests
./gradlew test

# Run Android tests (on device/emulator)
./gradlew connectedAndroidTest

# Run specific test class
./gradlew test --tests com.mecaroid.interdate.*
```

## 📊 Performance Optimizations

- **Core Library Desugaring** - Modern Java APIs on older devices
- **Image Optimization** - Glide handles caching, compression, and efficient loading
- **Database Indexing** - Firebase indexes for fast query performance
- **WorkManager** - Efficient background task scheduling
- **ProGuard** - Optimized code size and performance

## 📈 Analytics & Monitoring

- **Firebase Analytics** - Track user engagement and behavior
- **Firebase Crashlytics** - Real-time crash reporting and analysis
- **Performance Monitoring** - Built-in Firebase performance tracking

## 🤝 Dependencies Management

The project uses **Version Catalog** in `gradle/libs.versions.toml` for centralized dependency management:

```toml
[versions]
android-application = "8.x.x"
kotlin-android = "1.9.x"
firebase-bom = "33.0.0"
# ... more versions
```

This approach ensures consistency across the build and makes updates easier.

## 📝 Build Variants

- **Debug** - Development builds with full logging
- **Release** - Optimized builds with ProGuard obfuscation

## ⚙️ Configuration Files

- `settings.gradle.kts` - Project-wide settings and repository configuration
- `gradle.properties` - Gradle daemon and system properties
- `local.properties` - Local SDK paths (should not be committed)

## 🔄 CI/CD & Deployment

- Supports Firebase App Distribution
- Automated crash reporting with Crashlytics
- Analytics for user behavior monitoring

## 📄 License

[Add your license information here]

## 👥 Author

**Thabelo Developer**  
GitHub: [ThabeloDeveloper](https://github.com/ThabeloDeveloper)

## 📞 Support

For issues, questions, or suggestions, please open an issue on the GitHub repository.

## 🎓 Learning Resources

### Android Development
- [Android Developer Documentation](https://developer.android.com)
- [Kotlin Language Guide](https://kotlinlang.org/docs/home.html)
- [Android Jetpack Components](https://developer.android.com/jetpack)

### Firebase
- [Firebase Documentation](https://firebase.google.com/docs)
- [Firebase Console](https://console.firebase.google.com)

### Best Practices
- [Google Architecture Guides](https://developer.android.com/topic/architecture)
- [Material Design](https://material.io/design)

---

**Last Updated:** February 19, 2026  
**Version:** 2026.01.12
