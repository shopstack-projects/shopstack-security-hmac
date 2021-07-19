plugins {
    java
    id("io.freefair.lombok") version Versions.lombokPlugin
    checkstyle
    jacoco
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
    testImplementation(Dependencies.mockitoInline)
    testImplementation(Dependencies.mockitoJunitJupiter)
}

checkstyle {
    toolVersion = Versions.checkstyle
}

jacoco {
    toolVersion = Versions.jacoco
}

tasks {
    test {
        useJUnitPlatform {
            maxParallelForks = Runtime.getRuntime().availableProcessors()
        }
        failFast = true
    }

    jar {
        from(sourceSets.main.get().allSource)
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
        }
    }
}