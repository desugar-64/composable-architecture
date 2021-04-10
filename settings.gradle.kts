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
    ":composable-architecture",
    ":data",
    ":common:common-ui",
    ":common:feature-home",
//    ":common:feature-movies-list",
    ":app",
    ":desktop",
    ":feature-movies-list",
    ":feature-movie",
    ":feature-movies-favorite",
    ":common-ui"
)
