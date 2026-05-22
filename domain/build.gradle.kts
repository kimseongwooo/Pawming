plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":model"))
    implementation(libs.javax.inject)
}
