import com.syaremych.composableArchitecture.buildsrc.Libs

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        // __LATEST_COMPOSE_RELEASE_VERSION__
        classpath("org.jetbrains.compose:compose-gradle-plugin:0.4.0-build184")
        classpath(com.syaremych.composableArchitecture.buildsrc.Libs.androidGradlePlugin)
        classpath(kotlin("gradle-plugin", version = com.syaremych.composableArchitecture.buildsrc.Libs.Kotlin.version))
        classpath(kotlin("serialization", version = com.syaremych.composableArchitecture.buildsrc.Libs.Kotlin.version))
        classpath(com.syaremych.composableArchitecture.buildsrc.Libs.AndroidX.Navigation.safeArgsPlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().forEach {
        it.kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlin.Experimental",
                "-Xallow-jvm-ir-dependencies"
            )
            jvmTarget = Libs.Kotlin.jvm
            useIR = true
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
