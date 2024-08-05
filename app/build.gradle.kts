plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.sonarqube)
}

android {
    namespace = "dev.passwordless.sampleapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.passwordless.sampleapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.preference.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.retrofit)
    implementation(libs.jwtdecode)
    implementation(project(mapOf("path" to ":passwordless")))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}

sonar {
    properties {
        property("sonar.projectKey", "bitwarden_passwordless-android")
        property("sonar.organization", "bitwarden")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sources", "app/src/main/,passwordless/src/main/")
        property("sonar.tests", "app/src/test/,passwordless/src/test/")
    }
}

tasks {
    getByName("sonarqube") {
        dependsOn("check")
    }
}