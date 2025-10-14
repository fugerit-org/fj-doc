<#import '../flavour-macro.ftl' as fhm> <?xml version="1.0" encoding="UTF-8"?>
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
<@fhm.addFormattingPomProperties context=context/>
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
            <@fhm.addFormattingPomPlugin context=context/>
        </#if>
        </plugins>
    </build>

</project>
