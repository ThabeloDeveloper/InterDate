plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.mecaroid.interdate"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mecaroid.interdate"
        minSdk = 24
        targetSdk = 35
        versionCode = 12
        versionName = "2026.01.12"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    buildToolsVersion = "35.0.0"
    ndkVersion = "28.0.12433566 rc1"
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }



}

dependencies {

    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation (libs.appcompat)
    implementation (libs.lifecycle.process)
    implementation (libs.material)
    implementation (libs.constraintlayout)
    implementation(platform(libs.firebase.bom))
    implementation (libs.google.firebase.auth)
    implementation (libs.firebase.crashlytics)
    implementation (libs.firebase.analytics)
    implementation (libs.firebase.database)
    implementation (libs.firebase.storage)
    implementation (libs.shimmer)
    implementation (libs.recaptcha)

    implementation(platform(libs.okhttp.bom))

    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation (libs.play.services.location)
    implementation (libs.play.services.ads)
    implementation (libs.play.services.auth)
    implementation (libs.core.splashscreen)
    implementation (libs.glide)
    implementation (libs.commons.lang3)


    
    implementation (libs.activity.ktx)
    implementation (libs.photoview)
    implementation (libs.core.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.inappmessaging.display)


    annotationProcessor (libs.compiler)
    testImplementation (libs.junit)
    androidTestImplementation (libs.ext.junit)
    androidTestImplementation (libs.espresso.core)
    implementation(libs.work.runtime.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}