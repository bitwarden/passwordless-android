import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.vanniktech.maven.publish") version "0.28.0"
}

android {
    namespace = "dev.passwordless.android"
    compileSdk = 34

    defaultConfig {
        version = "1.0.4"
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "VERSION_NAME", "\"${project.version}\"")
        }

        release {
            buildConfigField("String", "VERSION_NAME", "\"${project.version}\"")
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
        coordinates("com.bitwarden", "passwordless-android", project.version.toString())
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

        configure(
            AndroidSingleVariantLibrary(
                // the published variant
                variant = "release",

                // whether to publish a sources jar
                sourcesJar = true,

                // whether to publish a javadoc jar
                publishJavadocJar = true,
            )
        )
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.logging.interceptor)
    implementation (libs.kotlinx.coroutines.play.services)

    testImplementation(libs.junit)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.mockito.core)
}
