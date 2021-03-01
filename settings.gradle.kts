dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
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
    ":common-ui"
)
