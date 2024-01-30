import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.vanniktech.maven.publish") version "0.27.0"
}

android {
    namespace = "dev.passwordless.android"
    compileSdk = 34

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    mavenPublishing {
        coordinates("com.bitwarden", "passwordless-android", "0.0.7")
        // publishToMavenCentral(SonatypeHost.DEFAULT)
        publishToMavenCentral(SonatypeHost.S01)
        signAllPublications()

        pom {
            name.set("Passwordless Android Client SDK")
            description.set("Passwordless Android Client SDK allows you to integrate Passwordless into your Android application.")
            inceptionYear.set("2024")
            url.set("https://www.github.com/bitwarden/passwordless-android/")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("jonashendrickx")
                    name.set("Jonas Hendrickx")
                    organization.set("Bitwarden")
                    organizationUrl.set("https://github.com/bitwarden")
                    url.set("https://github.com/jonashendrickx")
                }
            }
            scm {
                url.set("https://github.com/bitwarden/passwordless-android")
                connection.set("scm:git:git@github.com:passwordless/passwordless-android.git")
                developerConnection.set("scm:git:git@github.com:bitwarden/passwordless-android.git")
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.mockito:mockito-core:5.9.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("org.mockito:mockito-core:5.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.credentials:credentials:1.3.0-alpha01")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha01")
}
