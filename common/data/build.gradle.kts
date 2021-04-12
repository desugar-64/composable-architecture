import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.syaremych.composableArchitecture.buildsrc.Libs

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.github.gmazzo.buildconfig") version "3.0.0"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(Libs.Kotlin.Ktor.core)
                implementation(Libs.Kotlin.Ktor.client)
                implementation(Libs.Kotlin.Serialization.json)
                implementation(Libs.Kotlin.Ktor.logging)
                implementation(Libs.Kotlin.Ktor.serialization)
            }
        }

        val androidMain by getting {
            dependencies {}
        }
        val desktopMain by getting {
            dependencies {}
        }

//        val commonTest by getting {
//            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
//            }
//        }
    }

}

buildConfig {
    val localProps = gradleLocalProperties(rootDir)
    buildConfigField("String", "API_KEY", "\"${localProps.getProperty("api.key")}\"")
}

android {
    compileSdkVersion(Libs.AndroidSDK.compileSDK)
    buildToolsVersion(Libs.AndroidSDK.buildTools)

    val localProps = gradleLocalProperties(rootDir)

    defaultConfig {
        minSdkVersion(Libs.AndroidSDK.minSDK)
        targetSdkVersion(Libs.AndroidSDK.targetSDK)
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_KEY", "\"${localProps.getProperty("api.key")}\"")
    }

    compileOptions {
        sourceCompatibility = Libs.Kotlin.javaVersion
        targetCompatibility = Libs.Kotlin.javaVersion
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}