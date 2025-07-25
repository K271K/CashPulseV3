enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CashPulse"
include(":app")
include(":core:ui")
include(":core:domain")
include(":core:data")
include(":feature:expenses:presentation")
include(":feature:expenses:domain")
include(":core:navigation")
include(":feature:incomes:presentation")
include(":feature:incomes:domain")
include(":feature:account:presentation")
include(":feature:account:domain")
include(":feature:categories")
include(":feature:categories:presentation")
include(":feature:categories:domain")
include(":feature:settings:presentation")
include(":feature:settings:domain")
