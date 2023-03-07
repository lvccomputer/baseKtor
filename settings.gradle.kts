pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven ( "https://www.jitpack.io" )
    }
}
rootProject.name = "BaseKtorNetwork"
include (":app")
include(":network")
include(":core-db")
include(":common")
include(":girl-photo")
