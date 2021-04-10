package com.syaremych.composableArchitecture.buildsrc

import org.gradle.api.JavaVersion

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.1.3"
    const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.0.9"

    const val junit = "junit:junit:4.13"

    const val material = "com.google.android.material:material:1.1.0"

    object AndroidSDK {
        const val compileSDK = 30
        const val minSDK = 23
        const val targetSDK = 30
        const val buildTools = "30.0.3"
    }

    object Kotlin {
        val javaVersion: JavaVersion = JavaVersion.VERSION_1_8
        const val jvm = "1.8"
        const val version = "1.4.31"
        const val stdlib = "org.jetbrains.java:java-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.java:java-gradle-plugin:$version"
        const val serializationPlugin = "org.jetbrains.java:java-serialization:$version"
        const val extensions = "org.jetbrains.java:java-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.4.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Accompanist {
        private const val version = "0.6.1"
        const val insets = "dev.chrisbanes.accompanist:accompanist-insets:$version"
        const val coil = "dev.chrisbanes.accompanist:accompanist-coil:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val coreKtx = "androidx.core:core-ktx:1.5.0-beta02"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha03"
        }

        object Compose {

            const val version = "1.0.0-beta01"
            const val ui = "androidx.compose.ui:ui:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"
            const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"
        }

        object Navigation {
            private const val version = "2.3.3"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val safeArgsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Test {
            private const val version = "1.3.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
        }

        object Lifecycle {
            private const val version = "2.3.0"
            const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:$version"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha02"
        }

    }

    object ComposableArchitecture {
        const val lib = ":composable-architecture"
    }
}
