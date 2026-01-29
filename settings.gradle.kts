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
        maven { url =uri("https://storage.zego.im/maven") }
        maven{url = uri("https://jitpack.io")}
        maven{url = uri("https://oss.sonatype.org/content/repositories/snapshots/")}
        maven{
            url  = uri("https://cardinalcommerceprod.jfrog.io/artifactory/android")
            credentials {
                // Be sure to add these non-sensitive credentials in order to retrieve dependencies from
                // the private repository.
                username = System.getenv("paypal_sgerritz")
                password = System.getenv("JFROG_API_KEY")
            }
        }
    }
}

rootProject.name = "InterDate"
include(":app")
