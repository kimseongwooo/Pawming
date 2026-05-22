pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Pawming"
include(":app")
include(":design-system")
include(":model")
include(":domain")
include(":data")
include(":network")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:favorites:api")
include(":feature:favorites:impl")
include(":feature:shelter:api")
include(":feature:shelter:impl")
include(":feature:animal-detail:api")
include(":feature:animal-detail:impl")
include(":feature:shelter-detail:api")
include(":feature:shelter-detail:impl")
