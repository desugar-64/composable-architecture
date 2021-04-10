import com.syaremych.composableArchitecture.buildsrc.Libs
plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.compose")
}

android {
    compileSdkVersion(Libs.AndroidSDK.compileSDK)
    buildToolsVersion(Libs.AndroidSDK.buildTools)

    defaultConfig {
        minSdkVersion(Libs.AndroidSDK.minSDK)
        targetSdkVersion(Libs.AndroidSDK.targetSDK)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(file("consumer-rules.pro"))
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = Libs.Kotlin.jvm
        useIR = true
    }

    compileOptions {
        sourceCompatibility = Libs.Kotlin.javaVersion
        targetCompatibility = Libs.Kotlin.javaVersion
    }

    lintOptions {
        isAbortOnError = false
    }
}

dependencies {

    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.foundation)
    implementation(Libs.AndroidX.Compose.layout)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.materialIconsExtended)
    implementation(Libs.AndroidX.Compose.runtime)
    implementation(Libs.AndroidX.Compose.runtimeLivedata)
    implementation(Libs.AndroidX.Compose.tooling)
    implementation(Libs.AndroidX.Compose.test)
    implementation(Libs.Accompanist.coil)

    implementation(project(":composable-architecture"))
    api(project(":data"))

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
}