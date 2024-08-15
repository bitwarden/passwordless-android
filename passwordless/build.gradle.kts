plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
    alias(libs.plugins.sonarqube)
}

android {
    namespace = "dev.passwordless.android"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        version = libs.versions.passwordless.sdk
        minSdk = libs.versions.minSdk.get().toInt()
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
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
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

    detektPlugins(libs.detekt.detekt.formatting)
    detektPlugins(libs.detekt.detekt.rules)
}

detekt {
    autoCorrect = true
    config.from(files("$rootDir/detekt-config.yml"))
}

tasks {
    check {
        dependsOn("detekt")
    }

    withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.bitwarden"
            artifactId = "passwordless-android"
            version = project.version.toString()

            afterEvaluate {
                from(components["release"])
            }

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
                    connection = "scm:git:git@github.com:bitwarden/passwordless-android.git"
                    developerConnection = "scm:git:git@github.com:bitwarden/passwordless-android.git"
                }
            }
        }
    }

    repositories {
        maven {
            name = "S01"
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
    sign(publishing.publications)
}

sonar {
    properties {
        property("sonar.projectKey", "bitwarden_passwordless-android")
        property("sonar.organization", "bitwarden")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sources", "passwordless/src/main/")
        property("sonar.tests", "passwordless/src/test/")
        property("sonar.test.inclusions", "passwordless/src/test/")
        property("sonar.exclusions", "passwordless/src/test/")
    }
}

tasks {
    getByName("sonarqube") {
        dependsOn("check")
    }
}
