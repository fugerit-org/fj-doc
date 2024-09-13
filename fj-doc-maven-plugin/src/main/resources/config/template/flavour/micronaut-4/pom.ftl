<#import '../flavour-macro.ftl' as fhm>
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>${context.groupId}</groupId>
  <artifactId>${context.artifactId}</artifactId>
  <version>${context.projectVersion}</version>
  <packaging>${r"${packaging}"}</packaging>

  <parent>
    <groupId>io.micronaut.platform</groupId>
    <artifactId>micronaut-parent</artifactId>
    <version>4.6.2</version>
  </parent>
  <properties>
    <packaging>jar</packaging>
    <jdk.version>${context.javaRelease}</jdk.version>
    <release.version>${context.javaRelease}</release.version>
    <micronaut.version>4.5.0</micronaut.version>
    <micronaut.aot.enabled>false</micronaut.aot.enabled>
    <micronaut.aot.packageName><@fhm.toProjectPackage context=context/>.aot.generated</micronaut.aot.packageName>
    <micronaut.runtime>netty</micronaut.runtime>
    <exec.mainClass><@fhm.toProjectPackage context=context/>.Application</exec.mainClass>
  </properties>

  <repositories>
    <repository>
      <id>central</id>
      <url>https://repo.maven.apache.org/maven2</url>
    </repository>
  </repositories>

  <dependencies>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-http-server-netty</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.serde</groupId>
      <artifactId>micronaut-serde-jackson</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.yaml</groupId>
      <artifactId>snakeyaml</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.openapi</groupId>
      <artifactId>micronaut-openapi-annotations</artifactId>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-http-client</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.test</groupId>
      <artifactId>micronaut-test-junit5</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>io.micronaut.maven</groupId>
        <artifactId>micronaut-maven-plugin</artifactId>
        <configuration>
          <configFile>aot-${r"${packaging}"}.properties</configFile>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <!-- Uncomment to enable incremental compilation -->
          <!-- <useIncrementalCompilation>false</useIncrementalCompilation> -->

          <annotationProcessorPaths combine.children="append">
            <#if context.addLombok >
            <path>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>1.18.34</version>
            </path>
            </#if>
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-http-validation</artifactId>
              <version>${r"${micronaut.core.version}"}</version>
            </path>
            <path>
              <groupId>io.micronaut.openapi</groupId>
              <artifactId>micronaut-openapi</artifactId>
              <version>${r"${micronaut.openapi.version}"}</version>
              <exclusions>
                <exclusion>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-inject</artifactId>
                </exclusion>
              </exclusions>
            </path>
            <path>
              <groupId>io.micronaut.serde</groupId>
              <artifactId>micronaut-serde-processor</artifactId>
              <version>${r"${micronaut.serialization.version}"}</version>
              <exclusions>
                <exclusion>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-inject</artifactId>
                </exclusion>
              </exclusions>
            </path>
          </annotationProcessorPaths>
          <compilerArgs>
            <arg>-Amicronaut.processing.group=org.fugerit.java.demo</arg>
            <arg>-Amicronaut.processing.module=fugerit-demo-micronaut</arg>
          </compilerArgs>
        </configuration>
      </plugin>
    </plugins>
  </build>

</project>
