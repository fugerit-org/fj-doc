plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${r"${quarkusPlatformGroupId}"}:${r"${quarkusPlatformArtifactId}"}:${r"${quarkusPlatformVersion}"}"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-arc")
    <#if context.addJacoco >
    implementation("io.quarkus:quarkus-jacoco")
    </#if>
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "${context.groupId}"
version = "${context.projectVersion}"

java {
    sourceCompatibility = JavaVersion.VERSION_${context.javaRelease}
    targetCompatibility = JavaVersion.VERSION_${context.javaRelease}
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
