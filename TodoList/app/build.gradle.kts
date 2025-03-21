import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

android {
    namespace = "com.example.todolist"
    compileSdk = 35

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }

    val NAVER_CLIENT_ID = localProperties.getProperty("NAVER_CLIENT_ID") ?: ""
    val NAVER_CLIENT_SECRET = localProperties.getProperty("NAVER_CLIENT_SECRET") ?: ""

    defaultConfig {
        applicationId = "com.example.todolist"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "NAVER_CLIENT_ID", "\"$NAVER_CLIENT_ID\"")
        resValue("string", "NAVER_CLIENT_ID", NAVER_CLIENT_ID)
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"$NAVER_CLIENT_SECRET\"")
        resValue("string", "NAVER_CLIENT_SECRET", NAVER_CLIENT_SECRET)
    }

    buildFeatures {
        //noinspection DataBindingWithoutKapt
        dataBinding = true
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.hilt.android.gradle.plugin)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":data")))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // naver
    implementation(libs.naver.oauth)
    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // todo
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    // recyclerView
    implementation(libs.androidx.recyclerview)
}