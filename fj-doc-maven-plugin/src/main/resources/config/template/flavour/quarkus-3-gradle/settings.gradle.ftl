pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        id "${r"${quarkusPluginId}"}" version "${r"${quarkusPluginVersion}"}"
    }
}
rootProject.name='${context.artifactId}'