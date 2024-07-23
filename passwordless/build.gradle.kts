plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "dev.passwordless.android"
    compileSdk = 34

    defaultConfig {
        version = "1.1.1"
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

    publishing {
        singleVariant("release")
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

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.bitwarden"
                artifactId = "passwordless-android"
                version = project.version.toString()

                pom {
                    name = "Passwordless Android Client SDK"
                    description = "Passwordless Android Client SDK allows you to integrate Passwordless into your Android application."
                    url = "https://www.github.com/bitwarden/passwordless-android/"
                    inceptionYear = "2024"

                    licenses {
                        license {
                            name = "GNU General Public License (GPL) v3.0"
                            url = "https://github.com/bitwarden/passwordless-android/blob/main/LICENSE_GPL.txt"
                            distribution = "https://github.com/bitwarden/passwordless-android/blob/main/LICENSE_GPL.txt"
                        }
                    }

                    developers {
                        developer {
                            id = "jonashendrickx"
                            name = "Jonas Hendrickx"
                            organization = "Bitwarden"
                            organizationUrl = "https://github.com/bitwarden"
                            url = "https://github.com/jonashendrickx"
                        }
                    }

                    scm {
                        url = "https://github.com/bitwarden/passwordless-android"
                        connection = "scm:git:git@github.com:passwordless/passwordless-android.git"
                        developerConnection = "scm:git:git@github.com:bitwarden/passwordless-android.git"
                    }
                }
            }
        }

        repositories {
            maven {
                name = " S01"
                val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

                credentials {
                    username = System.getenv("ORG_GRADLE_PROJECT_mavenCentralUsername") ?: throw Exception("'ORG_GRADLE_PROJECT_mavenCentralUsername' is missing.")
                    password = System.getenv("ORG_GRADLE_PROJECT_mavenCentralPassword") ?: throw Exception("'ORG_GRADLE_PROJECT_mavenCentralPassword' is missing.")
                }
            }
        }
    }

    signing {
        useInMemoryPgpKeys(
            System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey"),
            System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword")
        )
        sign(publishing.publications["release"])
    }
}