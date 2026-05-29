plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ru.mirea.kolpakovap.osmmaps"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.mirea.kolpakovap.osmmaps"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures {
            viewBinding = true
        }
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
    }
}

dependencies {
    // ANDROIDX PREFERENCE - для сохранения настроек карты
    // Нужен для кэширования тайлов и сохранения состояния
    implementation("androidx.preference:preference:1.2.1")
    // OSMDROID - библиотека для работы с OpenStreetMap
    // Версия 6.1.16 - стабильная версия с поддержкой всех функций
    implementation("org.osmdroid:osmdroid-android:6.1.16")
    implementation("com.yandex.android:maps.mobile:4.10.0-full")
    implementation("androidx.core:core:1.12.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}