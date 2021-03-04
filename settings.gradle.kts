dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
rootProject.name = "Movie Keeper Compose"
include(
    ":app",
    ":composable-architecture",
    ":feature-movies-list",
    ":data",
    ":feature-movie",
    ":feature-movies-favorite",
    ":common-ui",
    ":desktop",
    ":common:ui"
)
