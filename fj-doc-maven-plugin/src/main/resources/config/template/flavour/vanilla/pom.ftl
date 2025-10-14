<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>${context.groupId}</groupId>
    <artifactId>${context.artifactId}</artifactId>
    <version>${context.projectVersion}</version>

    <properties>
        <maven.compiler.release>${context.javaRelease}</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
<#if context.addJacoco >
        <jacoco-plugin-version>${context.defaultJacocoVersion}</jacoco-plugin-version>
</#if>
<#if context.addFormatting >
        <!-- formatter plugin -->
        <mvn-formatter-plugin-version>${context.defaultFormatterPluginVersion}</mvn-formatter-plugin-version>
        <fugerit-code-rules-version>${context.defaultFugeritCodeRulesVersion}</fugerit-code-rules-version>
        <format.skip>${context.defaultFormatSkip}</format.skip>
</#if>
    </properties>

    <build>
        <plugins>
         <#if context.addJacoco >
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>${r"${jacoco-plugin-version}"}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </#if>
        <#if context.addFormatting >
            <plugin>
                <groupId>net.revelc.code.formatter</groupId>
                <artifactId>formatter-maven-plugin</artifactId>
                <version>${r"${mvn-formatter-plugin-version}"}</version>
                <dependencies>
                    <dependency>
                        <groupId>org.fugerit.java</groupId>
                        <artifactId>fugerit-code-rules</artifactId>
                        <version>${r"${fugerit-code-rules-version}"}</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <configFile>org/fugerit/java/coderules/eclipse-format.xml</configFile>
                    <skip>${r"${format.skip}"}</skip>
                </configuration>
                <executions>
                    <execution>
                        <id>format-sources</id>
                        <phase>process-sources</phase>
                        <goals>
                            <goal>format</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </#if>
        </plugins>
    </build>

</project>
