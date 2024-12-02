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
        /*
        maven { url = uri("http://dl.bintray.com/amulyakhare/maven")
                isAllowInsecureProtocol = true}

         */
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "king-of-the-castle-project"
include(":app")
 