import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.syaremych.composableArchitecture.buildsrc.Libs

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.gmazzo.buildconfig") version "3.0.0"
}

buildConfig {
    val localProps = gradleLocalProperties(rootDir)
    buildConfigField("String", "API_KEY", "\"${localProps.getProperty("api.key")}\"")
    buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).forEach {
    it.kotlinOptions {
        jvmTarget = Libs.Kotlin.jvm
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation(project(":composable-architecture"))

    api("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")
    implementation("com.squareup.okhttp3:okhttp:4.8.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    testImplementation("junit:junit:4.13")
    testImplementation("org.mockito:mockito-core:3.3.3")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}
