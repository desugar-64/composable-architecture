import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.syaremych.composableArchitecture.buildsrc.Libs

plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("plugin.serialization")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Libs.AndroidSDK.compileSDK)
    buildToolsVersion(Libs.AndroidSDK.buildTools)

    val localProps = gradleLocalProperties(rootDir)

    defaultConfig {
        applicationId("com.sergeyfitis.moviekeeper")
        minSdkVersion(Libs.AndroidSDK.minSDK)
        targetSdkVersion(Libs.AndroidSDK.targetSDK)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug_key.jks")
            storePassword = localProps.getProperty("sign.pass")
            keyPassword = localProps.getProperty("sign.pass")
            keyAlias = localProps.getProperty("sign.alias")
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = Libs.Kotlin.javaVersion
        targetCompatibility = Libs.Kotlin.javaVersion
    }

    kotlinOptions {
        jvmTarget = Libs.Kotlin.jvm
    }

    lint {
        isAbortOnError = false
    }


    buildFeatures {
        compose = true
        // Disable unused AGP features
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }

    packagingOptions {
        resources.pickFirsts.add("META-INF/AL2.0")
        resources.pickFirsts.add("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Lifecycle.lifecycleRuntime)
    implementation(Libs.AndroidX.Lifecycle.extensions)
    implementation(Libs.AndroidX.Lifecycle.lifecycleCommon)

    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.foundation)
    implementation(Libs.AndroidX.Compose.layout)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.materialIconsExtended)
    implementation(Libs.AndroidX.Compose.runtime)
    implementation(Libs.AndroidX.Compose.runtimeLivedata)
    implementation(Libs.AndroidX.Compose.tooling)
    implementation(Libs.AndroidX.Compose.test)

    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.uiKtx)

    implementation(project(":composable-architecture"))
    implementation(project(":feature-movies-list"))
    implementation(project(":feature-movie"))

    // temporary add data module until finish modularizing
    implementation(project(":data"))
    implementation(project(":feature-movies-favorite"))

    // tests tests tests!!!
    testImplementation(Libs.junit)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
}
