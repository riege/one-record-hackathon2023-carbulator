/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/8.0.2/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    mavenLocal()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/riege/packages")
        credentials {
            username = (project.findProperty("github.packages.access.user") ?: System.getenv("GITHUB_PACKAGES_ACCESS_USER")).toString()
            password = (project.findProperty("github.packages.access.token") ?: System.getenv("GITHUB_PACKAGES_ACCESS_TOKEN")).toString()
        }
    }
    maven {
        url = uri( "https://jitpack.io")
    }
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")

    // no longer using com.riege:one-record-ontologymodel:2.0.2 but
    // ONE Record data model in Java POJOs:
    // as per https://jitpack.io/#riege/one-record-ontologydatamodel
    implementation("com.github.riege:one-record-ontologydatamodel:0.2.3")
    // as per https://jitpack.io/#riege/one-record-jsonutils
    implementation("com.github.riege:one-record-jsonutils:0.9.2")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.jboss.resteasy:resteasy-jaxrs:3.0.12.Final")
    implementation("org.jboss.resteasy:jaxrs-api:3.0.12.Final")
    implementation("org.jboss.resteasy:resteasy-jaxb-provider:3.0.12.Final")
    implementation("org.jboss.resteasy:resteasy-jackson2-provider:3.0.12.Final")
    implementation("org.glassfish.jersey.core:jersey-client:2.34")
    implementation("org.glassfish.jersey.inject:jersey-hk2:2.28")
    implementation("org.glassfish.jersey.media:jersey-media-jaxb:2.28")

    // Commandline parsind
    implementation("commons-cli:commons-cli:1.5.0")
}

application {
    // Define the main class for the application.
    mainClass.set("com.riege.onerecord.carbulator.CarbulatorEngine")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
