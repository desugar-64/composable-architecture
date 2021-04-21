import com.syaremych.composableArchitecture.buildsrc.Libs
import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

kotlin {
    jvm("desktop")
    android()
    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
//                api(compose.material)
                api(Libs.Coroutines.core)
            }
        }
        named("androidMain") {
            dependencies {
//                api(Libs.AndroidX.appcompat)
//                api(Libs.AndroidX.coreKtx)
//                api(Libs.AndroidX.Compose.tooling)
//                api(Libs.AndroidX.Compose.uiUtil)
//                api(Libs.Accompanist.coil)
            }
        }
        named("desktopMain") {

        }
    }
}

android {
    compileSdkVersion(Libs.AndroidSDK.compileSDK)
    buildToolsVersion(Libs.AndroidSDK.buildTools)

    defaultConfig {
        minSdkVersion(Libs.AndroidSDK.minSDK)
        targetSdkVersion(Libs.AndroidSDK.targetSDK)
        versionCode = 1
        versionName = "1.0"
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

    dependencies {
        testImplementation("junit:junit:4.13")
        testImplementation("org.mockito:mockito-core:3.3.3")
        testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    }
}


tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).forEach {
    it.kotlinOptions {
        jvmTarget = Libs.Kotlin.jvm
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.FlowPreview"
        )
    }
}