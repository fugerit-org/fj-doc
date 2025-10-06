plugins {
    id 'java'
    id 'io.quarkus'
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation enforcedPlatform("${r"${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"}")
    implementation 'io.quarkus:quarkus-rest'
    implementation 'io.quarkus:quarkus-rest-jackson'
    implementation 'io.quarkus:quarkus-smallrye-openapi'
    implementation 'io.quarkus:quarkus-config-yaml'
    implementation 'io.quarkus:quarkus-arc'
    <#if context.addJacoco >
    implementation 'io.quarkus:quarkus-jacoco'
    </#if>
    testImplementation 'io.quarkus:quarkus-junit5'
    testImplementation 'io.rest-assured:rest-assured'
}

group '${context.groupId}'
version '${context.projectVersion}'

java {
    sourceCompatibility = JavaVersion.VERSION_${context.javaRelease}
    targetCompatibility = JavaVersion.VERSION_${context.javaRelease}
}

test {
    systemProperty "java.util.logging.manager", "org.jboss.logmanager.LogManager"
}

compileJava {
    options.encoding = 'UTF-8'
    options.compilerArgs << '-parameters'
}

compileTestJava {
    options.encoding = 'UTF-8'
}