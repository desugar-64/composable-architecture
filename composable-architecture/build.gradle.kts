import com.syaremych.composableArchitecture.buildsrc.Libs

plugins {
    kotlin("jvm")
}

dependencies {
    api(Libs.Coroutines.core)

    testImplementation("junit:junit:4.13")
    testImplementation("org.mockito:mockito-core:3.3.3")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).forEach {
    it.kotlinOptions {
        jvmTarget = Libs.Kotlin.jvm
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.FlowPrevie"
        )
    }
}