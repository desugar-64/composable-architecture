import com.syaremych.composableArchitecture.buildsrc.Libs
import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    android()
    jvm("desktop")
    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }
        named("androidMain") {
            dependencies {
                api(Libs.AndroidX.appcompat)
                api(Libs.AndroidX.coreKtx)
                api(Libs.AndroidX.Compose.tooling)
                api(Libs.AndroidX.Compose.uiUtil)
                api(Libs.Accompanist.coil)
            }
        }
        named("desktopMain") {
            dependencies {
                api(compose.desktop.common)
                implementation("com.alialbaali.kamel:kamel-image:0.2.0")
            }
        }
    }

    configurations {
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
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
}
