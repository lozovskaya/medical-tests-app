// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val room_version = "2.6.1"

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.googleKsp) apply false
    alias(libs.plugins.roomPlugin) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}