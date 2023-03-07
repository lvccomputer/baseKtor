// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    id ("com.android.application")  version "7.4.1" apply false
    id ("com.android.library" ) version "7.4.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.8.0" apply false
    id ("com.google.dagger.hilt.android") version "2.44" apply false
    id ("androidx.navigation.safeargs") version "2.5.3" apply false
}
buildscript {
    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.11.1")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.4")

    }
}