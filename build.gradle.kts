plugins {
    java
    id("io.freefair.lombok") version Versions.lombokPlugin
    checkstyle
}

group = "dev.shopstack.security"
version = "0.1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Dependencies.slf4jApi)
    testImplementation(Dependencies.slf4jSimpleLogger)

    testImplementation(Dependencies.junitJupiterApi)
    testRuntimeOnly(Dependencies.junitJupiterEngine)
    testImplementation(Dependencies.junitJupiterParams)
    testImplementation(Dependencies.assertjCore)
}

tasks {
    test {
        useJUnitPlatform {
            maxParallelForks = Runtime.getRuntime().availableProcessors()
        }
        failFast = true
    }

    checkstyle {
        toolVersion = Versions.checkstyle
    }
}